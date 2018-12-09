package vadc.heartbeat.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationEventJmsListener {

    public boolean isProcessed = false;

    @JmsListener(destination = "${hbp.in.queue}")
    public void processEvent(String payload) {
        log.info("Received event {}", payload);
        isProcessed = true;
    }
}
