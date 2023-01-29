package com.example.myandroidpro;

public class CommentModel {
        private String userId;
        private String postId;
        private String comment_text;
        private int likes;

    public CommentModel(String userId, String postId, String comment_text, int likes) {
        this.userId = userId;
        this.postId = postId;
        this.comment_text = comment_text;
        this.likes = likes;
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getPostId() { return postId; }

    public void setPostId(String postId) { this.postId = postId;  }

    public String getComment_text() { return comment_text; }

    public void setComment_text(String comment_text) { this.comment_text = comment_text;}

    public int getLikes() {  return likes; }

    public void setLikes(int likes) {  this.likes = likes;  }




}
