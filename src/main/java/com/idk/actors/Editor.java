package com.idk.actors;

import com.idk.models.News;
import com.idk.receivers.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;

public class Editor {

    @Autowired
    NewsService newsService;

    @JmsListener(destination = "Count_News_Read", containerFactory = "myFactory")
    public void receiveMessage(News news) {
        //newsRepository.increaseNewsCount(news.getId());
        System.out.println("Current readers for " + news.getId() + " is " + newsService.getReadersCount(news.getId()) + "\n");
    }

}
