package com.idk.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Destination;

import static com.idk.constants.NewsConstants.*;

@Configuration
public class SenderConfig {

    @Bean
    public ActiveMQConnectionFactory senderConnectionFactory() {
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");

        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(BROKER_URL);

        return activeMQConnectionFactory;
    }

    @Bean
    public Destination subscribeNewsResponseDestination() {
        return new ActiveMQQueue(DESTINATION_RECEIVED_SUBSCRIPTIONS);
    }

    @Bean
    public Destination readNewsResponseDestination() {
        return new ActiveMQQueue(DESTINATION_REQUESTED_NEWS_READ);
    }

    @Bean
    public Destination addNewsResponseDestination() {
        return new ActiveMQQueue(DESTINATION_REQUESTED_NEWS_READ);
    }

    @Bean
    public Destination deleteNewsResponseDestination() {
        return new ActiveMQQueue(DESTINATION_REQUESTED_NEWS_DELETED);
    }

    @Bean
    public Destination updateNewsResponseDestination() {
        return new ActiveMQQueue(DESTINATION_REQUESTED_NEWS_UPDATED);
    }
}
