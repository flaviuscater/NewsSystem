package com.idk.receivers;

import com.idk.models.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

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

    public Integer getReadersCount(String newsId) {
        return newsRepository.getReadersCount(newsId);
    }

}
