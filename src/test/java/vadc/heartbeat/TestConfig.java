package vadc.heartbeat;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@Configuration
public class TestConfig {

    @Bean
    @Profile("!travis")
    public LocalStackContainer localStackContainer() {
        LocalStackContainer localStackContainer = new LocalStackContainer().withServices(SQS);
        localStackContainer.start();
        return localStackContainer;
    }

    @Bean
    @Profile("!travis")
    @Primary
    public SQSConnectionFactory sqsConnectionFactory(LocalStackContainer localStackContainer) {
        AmazonSQS amazonSQS = AmazonSQSClient.builder()
                .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(SQS))
                .withCredentials(localStackContainer.getDefaultCredentialsProvider())
                .build();
        amazonSQS.createQueue("notification_events");
        return new SQSConnectionFactory(new ProviderConfiguration(), amazonSQS);
    }

    @Bean
    public JmsTemplate testJmsTemplate(SQSConnectionFactory sqsConnectionFactory) {
        return new JmsTemplate(sqsConnectionFactory);
    }
}
