package com.example.searchmap.model;

import java.io.Serializable;

public class Map implements Serializable {
    private String title;
    private Position position;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
