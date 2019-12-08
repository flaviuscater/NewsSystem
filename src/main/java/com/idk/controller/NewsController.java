package com.idk.controller;

import com.idk.actors.Editor;
import com.idk.actors.Reader;
import com.idk.models.Category;
import com.idk.models.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    Reader reader;

    @Autowired
    Editor editor;

    @PostMapping("/subscribe")
    public List<News> subscribeToNews(@RequestBody News news) throws JMSException {
        String messageId = reader.subscribeToNews(news.getCategory());
        return reader.receiveSubscribedNews(messageId);
    }

    @GetMapping("/read")
    public News readNews(@RequestBody News news) throws JMSException {
        String messageId = reader.readNews(news.getId());
        return reader.receiveReadNews(messageId);
    }

    @PostMapping("/add")
    public String addNews(@RequestBody News news) throws JMSException {
        String messageId = editor.addNews(news);
        return editor.receiveAddNewsResponse(messageId);
    }
}
