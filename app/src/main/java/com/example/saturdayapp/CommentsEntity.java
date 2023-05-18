package com.example.saturdayapp;

import java.sql.Timestamp;
import java.util.Date;

public class CommentsEntity {
    private String mainText, authorNickname, parentArticleID;
    private Long addTime;
    private Integer articleRate;

    public Integer getArticleRate() {
        return articleRate;
    }

    public String getMainText() {
        return mainText;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public Long getAddTime() {
        return addTime;
    }

    public String getParentArticleID() {
        return parentArticleID;
    }

    public CommentsEntity(){}
    public CommentsEntity(String mainText, String authorNickname, Long addTime, Integer articleRate, String parentArticleID) {
        this.mainText = mainText;
        this.authorNickname = authorNickname;
        this.addTime = addTime;
        this.articleRate = articleRate;
        this.parentArticleID = parentArticleID;
    }
}
