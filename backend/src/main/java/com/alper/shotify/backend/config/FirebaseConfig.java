package com.alper.shotify.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() throws IOException {
        String firebaseAdminBase64 = System.getenv("FIREBASE_ADMIN_CONFIG");
        if (firebaseAdminBase64 == null || firebaseAdminBase64.isEmpty()) {
            throw new RuntimeException("FIREBASE_ADMIN_CONFIG environment variable not set");
        }

        byte[] decodedBytes = Base64.getDecoder().decode(firebaseAdminBase64);
        InputStream serviceAccountStream = new ByteArrayInputStream(decodedBytes);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                .setStorageBucket("shotify-d5ae8.firebasestorage.app")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
