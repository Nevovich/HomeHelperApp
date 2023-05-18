package com.example.saturdayapp;

import android.content.Intent;

public class ListEntity {
    private String header;
    private String articleID;
    private String description;
    private String videoLink;
    private String authorID;
    private Integer taskTime;

    public ListEntity() {
    }

    public ListEntity(String articleID, String header, String description, String videoLink, String authorID, Integer taskTime) {
        this.articleID = articleID;
        this.header = header;
        this.description = description;
        this.videoLink = videoLink;
        this.authorID = authorID;
    }

    public Integer getTaskTime() {
        return taskTime;
    }

    public String getAuthorID() { return authorID; }

    public void setArticleID(String articleID) { this.articleID = articleID; }

    public String getArticleID() { return articleID; }

    public String getHeader() { return header; }

    public String getVideoLink() { return videoLink; }

    public String getDescription() { return description; }
}
