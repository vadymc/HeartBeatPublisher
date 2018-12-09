package vadc.heartbeat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import vadc.heartbeat.listener.NotificationEventJmsListener;

import static org.awaitility.Awaitility.await;

public class IncomingEventProcessingTest extends AbstractIntTest {

    @Autowired
    private JmsTemplate testJmsTemplate;

    @Autowired
    private NotificationEventJmsListener notificationEventJmsListener;

    @Value("${hbp.in.queue}")
    private String incomingQueue;

    @Test
    public void testIncomingEventProcessing() {
        testJmsTemplate.convertAndSend(incomingQueue, "{testbody}");

        await()
                .until(() -> notificationEventJmsListener.isProcessed);
    }
}
