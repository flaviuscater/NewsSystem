package com.idk.senders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import java.util.concurrent.atomic.AtomicReference;

import static com.idk.constants.NewsConstants.*;

@Component
public class NewsDeleteSender {

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("deleteNewsResponseDestination")
    private Destination requestedDeletedNewsDestination;

    public String deletedNews(String newsId) throws JMSException {
        final AtomicReference<Message> message = new AtomicReference<>();

        jmsTemplate.convertAndSend(DELETE_NEWS_EVENT, newsId, messagePostProcessor -> {
            message.set(messagePostProcessor);
            return messagePostProcessor;
        });

        String messageId = message.get().getJMSMessageID();
        System.out.format("Deleting news with id = %s with MessageId=%s\n",
                newsId, messageId);

        return messageId;
    }

    public String receiveDeleteNewsResponse(String correlationId) {
        String result = (String) jmsTemplate.receiveSelectedAndConvert(requestedDeletedNewsDestination, "JMSCorrelationID = '" + correlationId + "'");
        System.out.format("receive Status=%s for CorrelationId=%s\n", result, correlationId);
        return result;
    }

}
