package com.hadjmohamed.hirfati;

import android.net.Uri;

public class Category {
    String categoryId;
    String categoryName;
    Uri image;

    public Category(String categoryName, Uri image) {
        this.categoryName = categoryName;
        this.image = image;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
