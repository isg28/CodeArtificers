package com.codeartificers.schedulingapp.model;

import org.springframework.data.annotation.Id;

public class UserCounter {
    @Id
    private String id;
    private String name;
    private long sequence;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }
}
