package com.idk.receivers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.TextMessage;

import static com.idk.constants.NewsConstants.*;

@Service
public class DeleteNewsReceiver {

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("deleteNewsResponseDestination")
    private Destination requestedDeletedNewsDestination;

    @JmsListener(destination = DELETE_NEWS_EVENT, containerFactory = "myFactory")
    public void deleteNewsEventHandler(String newsId, @Header(JmsHeaders.MESSAGE_ID) String messageId) {

        newsRepository.deleteNews(newsId);

        System.out.println("Deleted:" + newsId);

        jmsTemplate.send(requestedDeletedNewsDestination, messageCreator -> {
            TextMessage message = messageCreator.createTextMessage("Deleted Successfully");
            message.setJMSCorrelationID(messageId);
            return message;
        });
    }
}
