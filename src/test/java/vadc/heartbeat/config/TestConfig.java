package vadc.heartbeat.config;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static org.mockito.Mockito.mock;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@Configuration
public class TestConfig {

    @Value("${hbp.in.queue}")
    private String incomingQueue;

    @Bean
    @Profile("localtest")
    public LocalStackContainer localStackContainer() {
        LocalStackContainer localStackContainer = new LocalStackContainer().withServices(SQS);
        localStackContainer.start();
        return localStackContainer;
    }

    @Bean
    @Profile("localtest")
    @Primary
    public SQSConnectionFactory sqsConnectionFactory(LocalStackContainer localStackContainer) {
        AmazonSQS amazonSQS = AmazonSQSClient.builder()
                .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(SQS))
                .withCredentials(localStackContainer.getDefaultCredentialsProvider())
                .build();
        amazonSQS.createQueue(incomingQueue);
        return new SQSConnectionFactory(new ProviderConfiguration(), amazonSQS);
    }

    @Bean
    public JmsTemplate testJmsTemplate(SQSConnectionFactory sqsConnectionFactory) {
        return new JmsTemplate(sqsConnectionFactory);
    }

    @Bean
    public RestTemplate firebaseRestTemplate() {
        return mock(RestTemplate.class);
    }
}
