package com.hadjmohamed.hirfati;

import android.net.Uri;

public class Comment {

    private Uri image;
    private String name;
    private String text;

    public Comment(Uri image, String name, String text) {
        this.image = image;
        this.name = name;
        this.text = text;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
