package com.example.zodiac.sawa.models;

/**
 * Created by Rabee on 4/24/2017.
 */

public class SettingsRecyclerViewDataProvider {
    int image;
    String text;

    public SettingsRecyclerViewDataProvider(int image, String text) {
        this.image = image;
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
