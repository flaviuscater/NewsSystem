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
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import static com.idk.constants.NewsConstants.ADD_NEWS_EVENT;

@Service
public class AddNewsReceiver {

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("addNewsResponseDestination")
    Destination addNewsResponseDestination;

    @JmsListener(destination = ADD_NEWS_EVENT, containerFactory = "myFactory")
    public void addNewsEventHandler(News news, @Header(JmsHeaders.MESSAGE_ID) String messageId) {
        newsRepository.addNews(news);
        System.out.println("Current news:" + newsRepository.getNewsList());


        jmsTemplate.send(addNewsResponseDestination, messageCreator -> {
            TextMessage message = messageCreator.createTextMessage("Success");
            message.setJMSCorrelationID(messageId);
            return message;
        });
    }
}
