package com.idk.receivers;

import com.idk.models.Category;
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
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import static com.idk.constants.NewsConstants.DESTINATION_NEWS_SUBSCRIBE;

@Service
public class SubscribeNewsService {

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("receivedSubscriptionsDestination")
    private Destination receivedSubscriptionsDestination;

    @JmsListener(destination = DESTINATION_NEWS_SUBSCRIBE, containerFactory = "myFactory")
    public void subscribeToNewsEventHandler(Category category, @Header(JmsHeaders.MESSAGE_ID) String messageId) {
        System.out.format("Received subscribe to news with category='%s' with MessageId='%s'\n", category, messageId);

        List<News> newsList = newsRepository.getNewsList();
        List<News> filteredNews = newsList.stream()
                .filter(news -> news.getCategory().equals(category)).collect(Collectors.toList());

        jmsTemplate.send(receivedSubscriptionsDestination, messageCreator -> {
            ObjectMessage message = messageCreator.createObjectMessage();
            message.setObject((Serializable) filteredNews);
            message.setJMSCorrelationID(messageId);
            return message;
        });
    }
}
