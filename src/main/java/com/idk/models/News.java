package com.idk.models;

import java.io.Serializable;
import java.time.LocalDate;

public class News implements Serializable {

    public String id;
    private LocalDate publishedDate;
    private LocalDate lastModified;
    private String source;
    private String author;
    private String body;
    private Category category;

    public News(String id, LocalDate publishedDate, LocalDate lastModified, String source, String author, String body, Category category) {
        this.id = id;
        this.publishedDate = publishedDate;
        this.lastModified = lastModified;
        this.source = source;
        this.author = author;
        this.body = body;
        this.category = category;
    }

    public News(LocalDate lastModified, String body) {
        this.lastModified = lastModified;
        this.body = body;
    }

    public News() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "News{" +
                "id='" + id + '\'' +
                ", publishedDate=" + publishedDate +
                ", lastModified=" + lastModified +
                ", source='" + source + '\'' +
                ", author='" + author + '\'' +
                ", body='" + body + '\'' +
                ", category=" + category +
                '}';
    }
}