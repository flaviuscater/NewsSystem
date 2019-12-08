package com.idk.service;

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
import javax.jms.TextMessage;
import java.util.List;
import java.util.stream.Collectors;

import static com.idk.constants.NewsConstants.DESTINATION_NEWS_SUBSCRIBE;

@Service
public class NewsService {

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("statusDestination")
    private Destination statusDestination;

    @JmsListener(destination = "Add_News_Event", containerFactory = "myFactory")
    public void addNewsEventHandler(News news) {
        newsRepository.addNews(news);
    }

    @JmsListener(destination = "Update_News_Event", containerFactory = "myFactory")
    public void updateNewsEventHandler(String newsId, News news) {
        newsRepository.updateNews(newsId, news);
    }

    @JmsListener(destination = "Delete_News_Event", containerFactory = "myFactory")
    public void deleteNewsEventHandler(String newsId) {
        newsRepository.deleteNews(newsId);
    }

    @JmsListener(destination = DESTINATION_NEWS_SUBSCRIBE, containerFactory = "myFactory")
    public void subscribeToNewsEventHandler(Category category, @Header(JmsHeaders.MESSAGE_ID) String messageId) {
        System.out.format("Received subscribe to news with category='%s' with MessageId='%s'", category, messageId);

//        List<News> newsList = newsRepository.getNewsList();
//        List<News> filteredNews = newsList.stream()
//                .filter(news -> news.getCategory().equals(category)).collect(Collectors.toList());
//
//        filteredNews.forEach(news -> newsRepository.increaseNewsCount(news.getId()));

        jmsTemplate.send(statusDestination, messageCreator -> {
            TextMessage message = messageCreator.createTextMessage("Accepted");
            message.setJMSCorrelationID(messageId);
            return message;
        });

        //jmsTemplate.convertAndSend("Count_News_Read", Arrays.asList());
    }

    public Integer getReadersCount(String newsId) {
        return newsRepository.getReadersCount(newsId);
    }


}
