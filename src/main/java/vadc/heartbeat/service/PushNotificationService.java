package vadc.heartbeat.service;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class PushNotificationService {

    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/v1/projects/heartbeatpublisher/messages:send";

    @Autowired
    private RestTemplate firebaseRestTemplate;

    public void send(String body) {
        try {
            firebaseRestTemplate.postForObject(FIREBASE_API_URL, buildNotificationMessage(body).toString(), String.class);
            log.info("Successfully pushed event {}", body);
        } catch (RestClientException e) {
            log.error("Failed to push event {}", body, e);
        }
    }

    private static JsonObject buildNotificationMessage(String event) {
        JsonObject jNotification = new JsonObject();
        jNotification.addProperty("title", "Event");
        jNotification.addProperty("body", event);

        JsonObject jMessage = new JsonObject();
        jMessage.add("notification", jNotification);
        jMessage.addProperty("topic", "notification_events");

        JsonObject jFcm = new JsonObject();
        jFcm.add("message", jMessage);

        return jFcm;
    }

}
