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

import static com.idk.constants.NewsConstants.UPDATE_NEWS_EVENT;

@Component
public class NewsUpdateSender {

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("updateNewsResponseDestination")
    private Destination updateNewsResponseDestination;

    public String updateNews(News news) throws JMSException {
        final AtomicReference<Message> message = new AtomicReference<>();

        jmsTemplate.convertAndSend(UPDATE_NEWS_EVENT, news, messagePostProcessor -> {
            message.set(messagePostProcessor);
            return messagePostProcessor;
        });

        String messageId = message.get().getJMSMessageID();
        System.out.format("Updating news with id = %s with MessageId=%s\n",
                news.getId(), messageId);

        return messageId;
    }

    public String receiveUpdateNewsResponse(String correlationId) {
        String result = (String) jmsTemplate.receiveSelectedAndConvert(updateNewsResponseDestination, "JMSCorrelationID = '" + correlationId + "'");
        System.out.format("receive Status=%s for CorrelationId=%s\n", result, correlationId);
        return result;
    }
}
