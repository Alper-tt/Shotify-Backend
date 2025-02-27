import pandas as pd
from flask import Flask, request, jsonify
import ssl
import nltk
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import pika
import json
import threading

df = pd.read_csv('turkish_song_lyrics.csv')
try:
    _create_default_https_context = ssl._create_default_https_context
except AttributeError:
    pass
else:
    ssl._create_default_https_context = ssl._create_unverified_context

nltk.download('punkt')
nltk.download('stopwords')
nltk.download('punkt_tab')
stop_words = set(stopwords.words('turkish'))
df['songId'] = df.index + 1

def preprocess_text(text):
    text = text.lower()
    tokens = word_tokenize(text)
    tokens = [word for word in tokens if word.isalnum() and word not in stop_words]
    return ' '.join(tokens)

df['processed_lyrics'] = df['lyrics'].apply(preprocess_text)
vectorizer = TfidfVectorizer()
X = vectorizer.fit_transform(df['processed_lyrics'])

def get_recommended_songs(user_input, top_n=5):
    processed_input = preprocess_text(user_input)
    user_vec = vectorizer.transform([processed_input])
    similarities = cosine_similarity(user_vec, X).flatten()
    top_indices = similarities.argsort()[-top_n:][::-1]
    return df.iloc[top_indices][['songId']]

app = Flask(__name__)

@app.route('/recommend-songs', methods=['POST'])
def recommend_songs_route():
    data = request.json
    if not data or 'keywords' not in data:
        return jsonify({"error": "No keywords provided"}), 400

    keywords = data['keywords']
    recommended = get_recommended_songs(' '.join(keywords))
    song_ids = recommended['songId'].tolist()
    return jsonify({"recommendedSongIds": song_ids})

RABBITMQ_HOST = 'rabbitmq'
RABBITMQ_USERNAME = 'guest'
RABBITMQ_PASSWORD = 'guest'
DETECTION_RESULTS_QUEUE = 'detection_results_queue'
RECOMMENDATION_QUEUE = 'recommendation_queue'

def publish_recommendation(result):
    """Öneri sonuçlarını recommendation_queue'ya gönderir."""
    credentials = pika.PlainCredentials(RABBITMQ_USERNAME, RABBITMQ_PASSWORD)
    connection = pika.BlockingConnection(
        pika.ConnectionParameters(host=RABBITMQ_HOST, credentials=credentials)
    )
    channel = connection.channel()
    channel.queue_declare(queue=RECOMMENDATION_QUEUE, durable=True)
    message = json.dumps(result)
    channel.basic_publish(exchange='', routing_key=RECOMMENDATION_QUEUE, body=message)
    connection.close()
    print("Published recommendation:", message)

def rabbitmq_callback(ch, method, properties, body):
    try:
        data = json.loads(body)
        print("Received detection results from queue:", data)
        if 'keywords' in data:
            keywords = data['keywords']
            recommended_df = get_recommended_songs(' '.join(keywords))
            song_ids = recommended_df['songId'].tolist()
            result = {"recommendedSongIds": song_ids}
            publish_recommendation(result)
        else:
            print("No 'keywords' found in the message")
        ch.basic_ack(delivery_tag=method.delivery_tag)
    except Exception as e:
        print("Error processing message:", e)
        ch.basic_nack(delivery_tag=method.delivery_tag, requeue=False)

def start_rabbitmq_consumer():
    credentials = pika.PlainCredentials(RABBITMQ_USERNAME, RABBITMQ_PASSWORD)
    connection = pika.BlockingConnection(
        pika.ConnectionParameters(host=RABBITMQ_HOST, credentials=credentials)
    )
    channel = connection.channel()
    channel.queue_declare(queue=DETECTION_RESULTS_QUEUE, durable=True)
    channel.basic_qos(prefetch_count=1)
    channel.basic_consume(queue=DETECTION_RESULTS_QUEUE, on_message_callback=rabbitmq_callback)
    print("Started consuming from", DETECTION_RESULTS_QUEUE)
    channel.start_consuming()

def start_consumer_in_thread():
    consumer_thread = threading.Thread(target=start_rabbitmq_consumer, daemon=True)
    consumer_thread.start()

if __name__ == '__main__':
    start_consumer_in_thread()
    app.run(host="0.0.0.0", port=5003)
