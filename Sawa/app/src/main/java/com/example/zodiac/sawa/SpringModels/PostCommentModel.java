package com.example.zodiac.sawa.SpringModels;

import java.util.List;

/**
 * Created by Rabee on 11/27/2017.
 */

public class PostCommentModel {
    PostResponseModel post;
    List<CommentModel> comments;
    public PostResponseModel getPost() {
        return post;
    }
    public void setPost(PostResponseModel post) {
        this.post = post;
    }
    public List<CommentModel> getComments() {
        return comments;
    }
    public void setComments(List<CommentModel> comments) {
        this.comments = comments;
    }
}
