package com.idk.senders;

import com.idk.models.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import java.util.concurrent.atomic.AtomicReference;

import static com.idk.constants.NewsConstants.READ_NEWS_EVENT;

@Component
public class NewsReadSender {

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("readNewsResponseDestination")
    private Destination requestedReadNewsDestination;

    public NewsReadSender() {
    }

    public String readNews(String newsId) throws JMSException {
        final AtomicReference<Message> message = new AtomicReference<>();

        jmsTemplate.convertAndSend(READ_NEWS_EVENT, newsId, messagePostProcessor -> {
            message.set(messagePostProcessor);
            return messagePostProcessor;
        });

        String messageId = message.get().getJMSMessageID();
        System.out.format("Reading news with id=%s with MessageId=%s\n",
                newsId, messageId);

        return messageId;
    }

    public News receiveReadNews(String correlationId) {
        News result = (News) jmsTemplate.receiveSelectedAndConvert(requestedReadNewsDestination, "JMSCorrelationID = '" + correlationId + "'");
        System.out.format("receive Status=%s for CorrelationId=%s\n", result, correlationId);
        return result;
    }
}
