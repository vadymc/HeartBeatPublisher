package vadc.heartbeat.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import vadc.heartbeat.service.PushNotificationService;

import java.io.IOException;

@Component
@Slf4j
public class NotificationEventJmsListener {

    @Autowired
    private PushNotificationService pushNotificationService;

    @JmsListener(destination = "${hbp.in.queue}")
    public void processEvent(String payload) throws IOException {
        pushNotificationService.send(payload);
    }
}
