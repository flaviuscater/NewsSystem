package com.idk.actors;

import com.idk.models.Category;
import com.idk.models.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.idk.constants.NewsConstants.DESTINATION_NEWS_SUBSCRIBE;

@Component
public class Reader {

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("statusDestination")
    private Destination statusDestination;

    //JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

    private List<News> subscribedNews;

    public Reader() {
        this.subscribedNews = new ArrayList<>();
    }

    // 1. trimite un event catre newsService ca s-a subscris,
    //2. in service se incrementeaza nr de cititori si se trimite catre editor sa afiseze nr de cititori actuali

    public String subscribeToNews(Category category) throws JMSException {
        final AtomicReference<Message> message = new AtomicReference<>();

        jmsTemplate.convertAndSend(DESTINATION_NEWS_SUBSCRIBE, category, messagePostProcessor -> {
            message.set(messagePostProcessor);
            return messagePostProcessor;
        });

        String messageId = message.get().getJMSMessageID();
        System.out.format("Subscribing for news with category=%s with MessageId=%s",
                category, messageId);

        return messageId;
    }

    public String receiveOrderStatus(String correlationId) {
        String status = (String) jmsTemplate.receiveSelectedAndConvert(
                statusDestination,
                "JMSCorrelationID = '" + correlationId + "'");

        System.out.format("receive Status=%s for CorrelationId=%s", status, correlationId);
        return status;
    }


    public List<News> getSubscribedNews() {
        return subscribedNews;
    }

    public void setSubscribedNews(List<News> subscribedNews) {
        this.subscribedNews = subscribedNews;
    };
}
