package com.idk.actors;

import com.idk.models.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.concurrent.atomic.AtomicReference;

import static com.idk.constants.NewsConstants.ADD_NEWS_EVENT;

@Component
public class AddNewsSender {

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("addNewsResponseDestination")
    Destination addNewsResponseDestination;

    public String addNews(News news) throws JMSException {
        final AtomicReference<Message> message = new AtomicReference<>();

        jmsTemplate.convertAndSend(ADD_NEWS_EVENT, news, messagePostProcessor -> {
            message.set(messagePostProcessor);
            return messagePostProcessor;
        });

        String messageId = message.get().getJMSMessageID();
        System.out.format("Adding news: %s with MessageId=%s\n",
                news, messageId);

        return messageId;
    }

    public String receiveAddNewsResponse(String correlationId) {
        String result = (String) jmsTemplate.receiveSelectedAndConvert(addNewsResponseDestination, "JMSCorrelationID = '" + correlationId + "'");
        System.out.format("receive Status=%s for CorrelationId=%s\n", result, correlationId);
        return result;
    }

}
