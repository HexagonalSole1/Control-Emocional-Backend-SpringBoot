package com.example.APISkeleton.configurations;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseInitializer {

    @PostConstruct
    public void initFirebase() {
        try {
            // 🔹 Cargar credenciales desde `resources/serviceAccountKey.json`
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("service.json");

            if (serviceAccount == null) {
                throw new IllegalStateException("Firebase service account key not found");
            }

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            System.out.println("✅ Firebase initialized successfully");

        } catch (IOException e) {
            throw new RuntimeException("🔥 Error initializing Firebase", e);
        }
    }
}

