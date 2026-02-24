package com.app.jobportal.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Job {
    private String id;
    private String title;
    private String description;
    private LocalDate postingDate;
    private LocalDateTime lastAccessDate;
    private int numViews = 0;

    public Job(String id, String title, String description, LocalDate postingDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.postingDate = postingDate;
        this.lastAccessDate = postingDate.atStartOfDay();
        this.numViews = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(LocalDate postingDate) {
        this.postingDate = postingDate;
    }

    public LocalDateTime getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(LocalDateTime lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public void addNumView() {
        numViews++;
    }

    public String toString() {
        return this.id + " " + this.title + "\n" +
                this.description + "\n" + "Posted on " + this.postingDate + "\n" +
                "Last access on " + this.lastAccessDate + "\n" +
                "Total views " + this.numViews;
    }
}
