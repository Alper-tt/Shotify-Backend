import pandas as pd
from flask import Flask, request, jsonify
import ssl
import nltk
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

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

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5003)
