package com.example.zodiac.sawa.SpringModels;

import java.util.List;

/**
 * Created by Rabee on 12/2/2017.
 */

public class ReactSingleModel {
    List<UserModel> users;
    int count;
    boolean myAction;

    public List<UserModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setMyAction(boolean myAction) {
        this.myAction = myAction;
    }

    public boolean getMyAction() {
        return myAction;
    }
}
