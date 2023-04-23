package com.example.saturdayapp;

public class ListEntity {
    private final String header;
    private final String description;

    public ListEntity(String header, String description) {
        this.header = header;
        this.description = description;
    }

    public String getHeader() {
        return header;
    }

    public String getDescription() {
        return description;
    }
}
