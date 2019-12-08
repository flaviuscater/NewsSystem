package com.idk.actors;

import com.idk.models.News;
import com.idk.senders.NewsAddSender;
import com.idk.senders.NewsDeleteSender;
import com.idk.senders.NewsUpdateSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

@Component
public class Editor {

    @Autowired
    NewsAddSender newsAddSender;

    @Autowired
    NewsDeleteSender newsDeleteSender;

    @Autowired
    NewsUpdateSender newsUpdateSender;

    // ADD NEWS
    public String addNews(News news) throws JMSException {
        return newsAddSender.addNews(news);
    }

    public String receiveAddNewsResponse(String correlationId) {
        return newsAddSender.receiveAddNewsResponse(correlationId);
    }

    // DELETE NEWS
    public String deleteNews(String newsId) throws JMSException {
        return newsDeleteSender.deletedNews(newsId);
    }

    public String receiveDeleteNewsResponse(String correlationId) {
        return newsDeleteSender.receiveDeleteNewsResponse(correlationId);
    }

    // UPDATE
    public String updateNews(News news) throws JMSException {
        return newsUpdateSender.updateNews(news);
    }

    public String receiveUpdateNewsResponse(String correlationId) {
        return newsUpdateSender.receiveUpdateNewsResponse(correlationId);
    }

}
