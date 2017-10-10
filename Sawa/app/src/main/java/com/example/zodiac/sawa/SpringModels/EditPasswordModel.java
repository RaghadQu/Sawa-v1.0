package com.example.zodiac.sawa.SpringModels;

/**
 * Created by zodiac on 10/10/2017.
 */

public class EditPasswordModel {


    int id;
    String old_password;
    String new_password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }



}
