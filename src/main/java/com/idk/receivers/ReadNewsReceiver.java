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
import java.util.List;
import java.util.Optional;

import static com.idk.constants.NewsConstants.READ_NEWS_EVENT;

@Service
public class ReadNewsReceiver {

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("readNewsResponseDestination")
    private Destination requestedReadNewsDestination;

    @JmsListener(destination = READ_NEWS_EVENT, containerFactory = "myFactory")
    public void readNewsEventHandler(String newsId, @Header(JmsHeaders.MESSAGE_ID) String messageId) {
        List<News> newsList = newsRepository.getNewsList();
        Optional<News> foundNews = newsList.stream()
                .filter(news -> news.getId().equals(newsId)).findFirst();

        // increase readers count
        foundNews.ifPresent(news -> newsRepository.increaseNewsReadCount(news.getId()));

        // GET Readers count
        System.out.println("Readers count: \n" + newsRepository.getReadersCount(newsId));

        jmsTemplate.send(requestedReadNewsDestination, messageCreator -> {
            ObjectMessage message = messageCreator.createObjectMessage();
            message.setObject(foundNews.orElse(null));
            message.setJMSCorrelationID(messageId);
            return message;
        });
    }
}
