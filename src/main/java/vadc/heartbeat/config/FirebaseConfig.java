package vadc.heartbeat.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("!test")
@Slf4j
public class FirebaseConfig {

    private static final List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/firebase.messaging");

    @Value("${HBP_FIREBASE_KEY}")
    private String firebaseApiKey;

    @PostConstruct
    public void init() throws IOException {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(getCredentialsStream()))
                .setDatabaseUrl("https://heartbeatpublisher.firebaseio.com/")
                .build();
        FirebaseApp.initializeApp(options);
    }

    @Bean
    public GoogleCredential googleCredential() throws IOException {
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(getCredentialsStream())
                .createScoped(SCOPES);
        googleCredential.refreshToken();
        return googleCredential;
    }

    @Bean
    public RestTemplate firebaseRestTemplate(GoogleCredential googleCredential) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            if (googleCredential.getExpiresInSeconds() < 60) {
                googleCredential.refreshToken();
                log.info("Refreshed google credential token");
            }
            request.getHeaders().add("Authorization", "Bearer " + googleCredential.getAccessToken());
            request.getHeaders().add("Content-Type", "application/json; UTF-8");
            return execution.execute(request, body);
        });
        return restTemplate;
    }


    private ByteArrayInputStream getCredentialsStream() {
        return new ByteArrayInputStream(firebaseApiKey.getBytes());
    }
}
