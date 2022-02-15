package me.erickmrtz.demo.firebase.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import me.erickmrtz.demo.firebase.DemoFirebaseApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

@Configuration
public class FirebaseConfig {

    @Value("${demo.google.firebase.configPath}")
    private String configPath;


    @PostConstruct
    public void init() throws IOException {
        ClassLoader classLoader = DemoFirebaseApplication.class.getClassLoader();

        File file = new File(Objects.requireNonNull(classLoader.getResource(configPath)).getFile());
        FileInputStream serviceAccount = new FileInputStream(file.getAbsolutePath());

        FirebaseOptions options = FirebaseOptions
                .builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }
}
