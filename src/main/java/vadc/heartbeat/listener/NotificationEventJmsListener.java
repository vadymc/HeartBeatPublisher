package vadc.heartbeat.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationEventJmsListener {

    @JmsListener(destination = "notification_events")
    public void processEvent(String payload) {
        log.info("Received event {}", payload);
    }
}
