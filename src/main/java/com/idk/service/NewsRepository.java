package com.idk.service;

import com.idk.models.Category;
import com.idk.models.News;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class NewsRepository {

    private List<News> newsList = Collections.synchronizedList(new ArrayList<>());

    Map<String, Integer> newsReadCount = Collections.synchronizedMap(new HashMap<>());

    @PostConstruct
    public void init() {
        //newsList.add(String id, LocalDate publishedDate, LocalDate lastModified, String source, String author, String body, Category category);
        newsList.add(new News("POLITICS_NEWS", LocalDateTime.now().toLocalDate(), LocalDateTime.now().toLocalDate(), "ProTv", "Basescu", "gagagas", Category.Politics));
        newsList.add(new News("SPORTS_NEWS", LocalDateTime.now().toLocalDate(), LocalDateTime.now().toLocalDate(), "DigiSport", "Basescu", "jfjru", Category.Sports));
        newsList.add(new News("LIFESTYLE_NEWS", LocalDateTime.now().toLocalDate(), LocalDateTime.now().toLocalDate(), "Antena1", "Iliescu", "zxvzv", Category.Lifestyle));

    }

    public NewsRepository() {
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void addNews(News news){
        this.newsList.add(news);
    }

    public void updateNews(String newsId, News news) {
        synchronized(newsList) {
            for(int i=0; i < newsList.size(); i++) {
                if (newsList.get(i).id.equals(newsId)) {
                    newsList.get(i).setBody(news.getBody());
                    newsList.get(i).setLastModified(LocalDateTime.now().toLocalDate());
                }
            }
        }
    }

    public void deleteNews(String newsId){
        newsList.removeIf(news -> news.getId().equals(newsId));
    }

    public void increaseNewsCount(String newsId) {
        newsReadCount.put(newsId, newsReadCount.get(newsId) + 1);
    }

    public void decreaseNewsCount(String newsId) {
        newsReadCount.put(newsId, newsReadCount.get(newsId) - 1);
    }

    public Integer getReadersCount(String newsId) {
        return newsReadCount.get(newsId);
    }

}
