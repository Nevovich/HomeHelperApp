package com.example.saturdayapp;

public class ListEntity {
    private String header;
    private String articleID;
    private String description;
    private String videoLink;

    public ListEntity() {
    }

    public ListEntity(String articleID, String header, String description, String videoLink) {
        this.articleID = articleID;
        this.header = header;
        this.description = description;
        this.videoLink = videoLink;
    }



    public String getArticleID() { return articleID; }

    public String getHeader() { return header; }

    public String getVideoLink() { return videoLink; }

    public String getDescription() { return description; }
}
