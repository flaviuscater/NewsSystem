package com.idk.controller;

import com.idk.actors.Reader;
import com.idk.models.Category;
import com.idk.models.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    Reader reader;

    @PostMapping("/subscribe")
    public String subscribeToNews(@RequestBody News news) throws JMSException {

        String messageId = reader.subscribeToNews(news.getCategory());

        String result = reader.receiveOrderStatus(messageId);

        return result;
    }
}
