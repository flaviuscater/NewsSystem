package com.idk.senders;

import com.idk.models.Category;
import com.idk.models.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.idk.constants.NewsConstants.SUBSCRIBE_NEWS_EVENT;

@Component
public class NewsSubscribeSender {

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("subscribeNewsResponseDestination")
    private Destination receivedSubscriptionsDestination;

    public NewsSubscribeSender() {
    }

    public String subscribeToNews(Category category) throws JMSException {
        final AtomicReference<Message> message = new AtomicReference<>();

        jmsTemplate.convertAndSend(SUBSCRIBE_NEWS_EVENT, category, messagePostProcessor -> {
            message.set(messagePostProcessor);
            return messagePostProcessor;
        });

        String messageId = message.get().getJMSMessageID();
        System.out.format("Subscribing for news with category=%s with MessageId=%s\n",
                category, messageId);

        return messageId;
    }

    public List<News> receiveSubscribedNews(String correlationId) {
        List<News> result = (List<News>) jmsTemplate.receiveSelectedAndConvert(receivedSubscriptionsDestination, "JMSCorrelationID = '" + correlationId + "'");
        System.out.format("receive Status=%s for CorrelationId=%s\n", result, correlationId);
        return result;
    }

}
