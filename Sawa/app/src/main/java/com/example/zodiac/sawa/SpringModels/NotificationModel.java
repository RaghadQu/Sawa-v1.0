package com.example.zodiac.sawa.SpringModels;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;


/**
 * Created by Rabee on 4/5/2017.
 */


public class NotificationModel {
    @SerializedName("sent_notification")
    List<Notification> sent_notification;

    @SerializedName("not_sent_notification")
    List<Notification> not_sent_notification;

    public List<Notification> getSent_notifications() {
        return sent_notification;
    }

    public void setSent_notifications(List<Notification> sent_notifications) {
        this.sent_notification = sent_notifications;
    }

    public List<Notification> getNot_sent_notifications() {
        return not_sent_notification;
    }

    public void setNot_sent_notifications(List<Notification> not_sent_notifications) {
        this.not_sent_notification = not_sent_notifications;
    }

    public class Notification {

        int id;
        UserModel friend1_id;
        UserModel friend2_id;
        PostRequestModel postId;
        FriendResponseModel friendshipId;
        int type;
        int read_flag;
        int sent_flag;
        private Date timestamp;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public UserModel getFriend1_id() {
            return friend1_id;
        }

        public void setFriend1_id(UserModel friend1_id) {
            this.friend1_id = friend1_id;
        }

        public UserModel getFriend2_id() {
            return friend2_id;
        }

        public void setFriend2_id(UserModel friend2_id) {
            this.friend2_id = friend2_id;
        }

        public PostRequestModel getPostId() {
            return postId;
        }

        public void setPostId(PostRequestModel postId) {
            this.postId = postId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getRead_flag() {
            return read_flag;
        }

        public void setRead_flag(int read_flag) {
            this.read_flag = read_flag;
        }

        public int getSent_flag() {
            return sent_flag;
        }

        public void setSent_flag(int sent_flag) {
            this.sent_flag = sent_flag;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public FriendResponseModel getFriendshipId() {
            return friendshipId;
        }

        public void setFriendshipId(FriendResponseModel friendshipId) {
            this.friendshipId = friendshipId;
        }


    }

}
