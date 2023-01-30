package com.example.myandroidpro;

public class CommentModel {
        private String userId;
        private String postId;
        private String content;
        private int likes;

    public CommentModel(String userId, String postId, String content, int likes) {
        this.userId = userId;
        this.postId = postId;
        this.content = content;
        this.likes = likes;
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getPostId() { return postId; }

    public void setPostId(String postId) { this.postId = postId;  }

    public String getContent() { return content; }

    public void setContent(String comment) { this.content = comment; }

    public int getLikes() {  return likes; }

    public void setLikes(int likes) {  this.likes = likes;  }




}
