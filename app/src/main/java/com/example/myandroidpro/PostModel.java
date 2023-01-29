package com.example.myandroidpro;

public class PostModel {
    private String _id;
    private String author;
    private String title;
    private String body;
    private int likes;

    public PostModel(String _id,String author, String title, String body, int likes) {
        this.author = author;
        this.title = title;
        this.body = body;
        this.likes = likes;
        this._id = _id;
    }

    public String get_id() { return _id; }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

}
