package com.idk.receivers;

import com.idk.models.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.TextMessage;

import static com.idk.constants.NewsConstants.DELETE_NEWS_EVENT;
import static com.idk.constants.NewsConstants.UPDATE_NEWS_EVENT;

@Service
public class UpdateNewsReceiver {

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("deleteNewsResponseDestination")
    private Destination requestedDeletedNewsDestination;

    @JmsListener(destination = UPDATE_NEWS_EVENT, containerFactory = "myFactory")
    public void deleteNewsEventHandler(News news, @Header(JmsHeaders.MESSAGE_ID) String messageId) {

        newsRepository.updateNews(news);

        System.out.println("Updated:" + news.getId());

        jmsTemplate.send(requestedDeletedNewsDestination, messageCreator -> {
            TextMessage message = messageCreator.createTextMessage("Updated Successfully");
            message.setJMSCorrelationID(messageId);
            return message;
        });
    }
}
