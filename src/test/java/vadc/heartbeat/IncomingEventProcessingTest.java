package vadc.heartbeat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class IncomingEventProcessingTest extends AbstractIntTest {

    @Autowired
    private JmsTemplate testJmsTemplate;

    @Autowired
    private RestTemplate firebaseRestTemplate;

    @Value("${hbp.in.queue}")
    private String incomingQueue;

    @Test
    public void testIncomingEventProcessing() {
        testJmsTemplate.convertAndSend(incomingQueue, "{testbody}");

        verify(firebaseRestTemplate, timeout(1000).times(1))
                .postForObject(any(String.class), any(String.class), any(Class.class));
    }
}
