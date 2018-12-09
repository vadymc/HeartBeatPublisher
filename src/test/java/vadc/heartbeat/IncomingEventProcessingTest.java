package vadc.heartbeat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import vadc.heartbeat.listener.NotificationEventJmsListener;

import static org.awaitility.Awaitility.await;

public class IncomingEventProcessingTest extends AbstractIntTest {

    @Autowired
    private JmsTemplate testJmsTemplate;

    @Autowired
    private NotificationEventJmsListener notificationEventJmsListener;

    @Test
    public void testIncomingEventProcessing() {
        testJmsTemplate.convertAndSend("test_notification_events", "{testbody}");

        await()
                .until(() -> notificationEventJmsListener.isProcessed);
    }
}
