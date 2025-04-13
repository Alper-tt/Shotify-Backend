import os
import tempfile
import subprocess
import json
import requests
from flask import Flask, request, jsonify, send_from_directory
import pika
from threading import Thread
import firebase_admin
from firebase_admin import credentials, storage
import uuid
from PIL import Image, ExifTags
import base64

app = Flask(__name__)

if not firebase_admin._apps:
    firebase_config_b64 = os.environ.get("FIREBASE_ADMIN_CONFIG")

    if not firebase_config_b64:
        raise ValueError("FIREBASE_ADMIN_CONFIG environment variable is not set")

    firebase_config_json = base64.b64decode(firebase_config_b64).decode("utf-8")
    cred = credentials.Certificate(json.loads(firebase_config_json))

    firebase_admin.initialize_app(cred, {
        'storageBucket': 'shotify-d5ae8.firebasestorage.app'
    })

VIDEO_CREATION_RESULTS_QUEUE = "video_creation_results_queue"

def download_file(url: str, ext: str) -> str:
    response = requests.get(url, stream=True)
    if response.status_code == 200:
        tmp_file = tempfile.NamedTemporaryFile(delete=False, suffix=ext)
        with open(tmp_file.name, 'wb') as f:
            for chunk in response.iter_content(chunk_size=8192):
                f.write(chunk)
        return tmp_file.name
    else:
        raise Exception(f"Failed to download file from {url}, status code: {response.status_code}")
    
def resize_image(image_path: str, max_resolution=(1920, 1080)) -> str:
    try:
        img = Image.open(image_path)

        try:
            exif = img._getexif()
            if exif:
                for tag, value in exif.items():
                    decoded = ExifTags.TAGS.get(tag, tag)
                    if decoded == "Orientation":
                        if value == 3:
                            img = img.rotate(180, expand=True)
                        elif value == 6:
                            img = img.rotate(270, expand=True)
                        elif value == 8:
                            img = img.rotate(90, expand=True)
        except Exception as e:
            print(f"Exif rotation skip: {e}")

        img.thumbnail(max_resolution)
        resized_path = image_path.replace(".jpg", "_resized.jpg")
        img.save(resized_path, "JPEG")
        return resized_path
    except Exception as e:
        print(f"Image resize failed: {e}")
        return image_path

def create_video_local(photo_path: str, audio_path: str) -> str:
    temp_dir = tempfile.gettempdir()
    output_file_name = f"{uuid.uuid4().hex}.mp4"
    output_video = os.path.join(temp_dir, output_file_name)
    command = [
        "ffmpeg",
        "-y",
        "-loop", "1",
        "-i", photo_path,
        "-i", audio_path,
        "-c:v", "libx264",
        "-tune", "stillimage",
        "-c:a", "aac",
        "-b:a", "192k",
        "-pix_fmt", "yuv420p",
        "-s", "1920x1080",
        "-shortest",
        output_video
    ]

    result = subprocess.run(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
    if result.returncode != 0:
        raise Exception(f"FFmpeg error: {result.stderr}")
    return output_video

def upload_video_to_firebase(video_path: str) -> str:
    bucket = storage.bucket()
    file_name = os.path.basename(video_path)
    blob = bucket.blob(f"videos/{file_name}")
    blob.upload_from_filename(video_path)
    blob.make_public()
    return blob.public_url

def process_video_request(photo_path: str, audio_url: str) -> str:
    photo_downloaded = False
    if photo_path.startswith("http"):
        local_photo = download_file(photo_path, ".jpg")
        local_photo = resize_image(local_photo)
        photo_downloaded = True
    else:
        local_photo = photo_path

    local_audio = download_file(audio_url, ".mp3")

    video_path = create_video_local(local_photo, local_audio)

    if photo_downloaded and os.path.exists(local_photo):
        os.remove(local_photo)
        print(f"Deleted temporary photo file: {local_photo}")
    if os.path.exists(local_audio):
        os.remove(local_audio)
        print(f"Deleted temporary audio file: {local_audio}")
    if photo_downloaded and os.path.exists(local_photo):
        os.remove(local_photo)

    firebase_video_url = upload_video_to_firebase(video_path)
    if os.path.exists(video_path):
        os.remove(video_path)
        print(f"Deleted temporary video file: {video_path}")

    return firebase_video_url

def publish_video_result(video_url: str, channel, correlation_id: str = None):
    result_message = json.dumps({"videoUrl": video_url})
    props = pika.BasicProperties()
    if correlation_id:
        props.correlation_id = correlation_id
    channel.basic_publish(
        exchange="",
        routing_key=VIDEO_CREATION_RESULTS_QUEUE,
        properties=props,
        body=result_message
    )
    print(f"Published video result with correlation_id: {correlation_id}.")

def rabbitmq_callback(ch, method, properties, body):
    try:
        data = json.loads(body)
        photo_path = data.get("photoPath")
        audio_url = data.get("audioUrl")
        if not photo_path or not audio_url:
            print("Missing photoPath or audioUrl in message.")
            ch.basic_ack(delivery_tag=method.delivery_tag)
            return

        print("Received video creation request:", data)
        video_url = process_video_request(photo_path, audio_url)
        print("Video created and uploaded to Firebase at:", video_url)
        correlation_id = properties.correlation_id if properties and properties.correlation_id else None
        publish_video_result(video_url, ch, correlation_id)
        ch.basic_ack(delivery_tag=method.delivery_tag)
    except Exception as e:
        print("Error processing video request:", e)
        ch.basic_nack(delivery_tag=method.delivery_tag, requeue=False)

def start_rabbitmq_consumer():
    rabbitmq_host = os.getenv("RABBITMQ_HOST", "rabbitmq")
    credentials = pika.PlainCredentials("guest", "guest")
    connection = pika.BlockingConnection(pika.ConnectionParameters(host=rabbitmq_host, credentials=credentials))
    channel = connection.channel()
    channel.queue_declare(queue="video_creation_queue", durable=True)
    channel.queue_declare(queue=VIDEO_CREATION_RESULTS_QUEUE, durable=True)
    channel.basic_consume(queue="video_creation_queue", on_message_callback=rabbitmq_callback)
    print("Video creation consumer started.")
    channel.start_consuming()

@app.route('/createVideo', methods=['POST'])
def create_video_endpoint():
    data = request.get_json()
    if not data or "photoPath" not in data or "audioUrl" not in data:
        return jsonify({"error": "photoPath and audioUrl required"}), 400
    try:
        video_url = process_video_request(data["photoPath"], data["audioUrl"])
        return jsonify({"videoUrl": video_url})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    consumer_thread = Thread(target=start_rabbitmq_consumer, daemon=True)
    consumer_thread.start()
    app.run(host="0.0.0.0", port=5007)