package com.idk.actors;

import com.idk.models.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

@Component
public class Editor {

    @Autowired
    AddNewsSender addNewsSender;

    // ADD NEWS
    public String addNews(News news) throws JMSException {
        return addNewsSender.addNews(news);
    }

    public String receiveAddNewsResponse(String correlationId) {
        return addNewsSender.receiveAddNewsResponse(correlationId);
    }

}
