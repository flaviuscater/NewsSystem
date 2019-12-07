package com.idk.service;

import com.idk.models.Category;
import com.idk.models.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    JmsTemplate jmsTemplate;

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

    @JmsListener(destination = "Subscribe_News_Event", containerFactory = "myFactory")
    public void subscribeToNewsletterEventHandler(Category category) {
        List<News> newsList = newsRepository.getNewsList();
        newsList.stream()
                .filter(news -> news.getCategory().equals(category))
                .forEach(news -> newsRepository.increaseNewsCount(news.getId()));

        //jmsTemplate.convertAndSend("Count_News_Read", Arrays.asList());
    }

    public Integer getReadersCount(String newsId) {
        return newsRepository.getReadersCount(newsId);
    }


}
