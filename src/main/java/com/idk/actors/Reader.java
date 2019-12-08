package com.idk.actors;

import com.idk.models.Category;
import com.idk.models.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;


@Component
public class Reader {

    private List<News> subscribedNews;

    @Autowired
    NewsSubscribeSender newsSubscribeSender;

    @Autowired
    NewsReadSender newsReadSender;

    public Reader() {
        this.subscribedNews = new ArrayList<>();
    }

    // SUBSCRIBE
    public String subscribeToNews(Category category) throws JMSException {
        return newsSubscribeSender.subscribeToNews(category);
    }

    public List<News> receiveSubscribedNews(String correlationId) {
        this.subscribedNews = newsSubscribeSender.receiveSubscribedNews(correlationId);
        return subscribedNews;
    }


    // READ
    public String readNews(String newsId) throws JMSException {
        return newsReadSender.readNews(newsId);
    }

    public News receiveReadNews(String correlationId) {
        return newsReadSender.receiveReadNews(correlationId);
    }

    //
}
