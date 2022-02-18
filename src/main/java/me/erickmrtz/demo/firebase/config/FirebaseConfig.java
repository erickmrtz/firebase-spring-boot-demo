package me.erickmrtz.demo.firebase.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import me.erickmrtz.demo.firebase.DemoFirebaseApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

@Configuration
public class FirebaseConfig {

    @Value("${demo.google.firebase.configPath}")
    private String configPath;

    @Value("${demo.google.firebase.projectId}")
    private String projectId;

    @Value("${demo.google.firebase.bucketId}")
    private String bucketId;

    @Bean(name = "firebaseStorage")
    public StorageOptions firebaseStorage() throws IOException {
        return StorageOptions
                .newBuilder()
                .setProjectId(projectId)
                .setCredentials(GoogleCredentials.fromStream(getServiceAccount()))
                .build();
    }

    @Bean(name = "bucketStorage")
    public String bucketStorage() {
        return bucketId;
    }

    @PostConstruct
    public void init() throws IOException {
        FirebaseOptions options = FirebaseOptions
                .builder()
                .setCredentials(GoogleCredentials.fromStream(getServiceAccount()))
                .build();

        FirebaseApp.initializeApp(options);
    }

    private FileInputStream getServiceAccount() throws FileNotFoundException {
        ClassLoader classLoader = DemoFirebaseApplication.class.getClassLoader();

        File file = new File(Objects.requireNonNull(classLoader.getResource(configPath)).getFile());
        return new FileInputStream(file.getAbsolutePath());
    }
}
