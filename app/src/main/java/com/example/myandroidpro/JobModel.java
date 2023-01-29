package com.example.myandroidpro;

import java.util.Date;

public class JobModel {
    private String _id;
    private String author;
    private String title;
    private String description;
    private String requirements;
    private int salary;
    private String location;
    private Date posted_date;

    public JobModel(String _id,String author,String title, String description, String requirements, int salary, String location,Date posted_date) {
        this._id = _id;
        this.author = author;
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.salary = salary;
        this.location = location;
        this.posted_date = posted_date;
    }

    public String get_id() { return _id; }

    public void set_id(String _id) { this._id = _id;  }

    public String getAuthor() { return author; }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Date getPosted_date() {
        return posted_date;
    }

    public void setPosted_date(Date posted_date) {
        this.posted_date = posted_date;
    }
}
