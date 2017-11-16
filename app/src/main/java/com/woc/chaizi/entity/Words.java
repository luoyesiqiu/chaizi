package com.woc.chaizi.entity;

/**
 * Created by zyw on 2016/4/9.
 */
public class Words {
    public String getChai() {
        return chai;
    }

    public void setChai(String chai) {
        this.chai = chai;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;


    private String source;
    private String chai;
}
