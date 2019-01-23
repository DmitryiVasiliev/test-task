package com.haulmont.testtask.Entity;

import java.io.Serializable;

public class Genre implements Serializable {
    private Long id;
    private String title;
    private int stat;


    public Genre(Long id, String title) {
        this.id = id;
        this.title = title;
        this.stat = stat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    @Override
    public String toString() {
        return this.getTitle();
    }
}
