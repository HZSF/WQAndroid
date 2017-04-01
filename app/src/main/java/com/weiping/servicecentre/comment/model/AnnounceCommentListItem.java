package com.weiping.servicecentre.comment.model;

public class AnnounceCommentListItem {
    public String id;
    public String customer_name;
    public String comment;
    public String announce_id;
    public String comment_time;
    public String session_id;
    public int numberOfComments;
    public int numberOfLike;
    public String[] comment_array;
    public String[] username_array;

    public boolean liked;

    public AnnounceCommentListItem(String id, String customer_name, String comment, String announce_id, String comment_time, String session_id,
                                   int numberOfLike, int numberOfComments, String[] comment_array, String[] username_array) {
        this.id = id;
        this.customer_name = customer_name;
        this.comment = comment;
        this.announce_id = announce_id;
        this.comment_time = comment_time;
        this.session_id = session_id;
        this.numberOfLike = numberOfLike;
        this.numberOfComments = numberOfComments;
        this.comment_array = comment_array;
        this.username_array = username_array;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAnnounce_id() {
        return announce_id;
    }

    public void setAnnounce_id(String announce_id) {
        this.announce_id = announce_id;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getNumberOfLike() {
        return numberOfLike;
    }

    public void setNumberOfLike(int numberOfLike) {
        this.numberOfLike = numberOfLike;
    }

    public String getSession_id() {
        return session_id;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public String[] getComment_array() {
        return comment_array;
    }

    public String[] getUsername_array() {
        return username_array;
    }
}
