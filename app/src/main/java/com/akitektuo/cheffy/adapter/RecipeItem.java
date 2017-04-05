package com.akitektuo.cheffy.adapter;

/**
 * Created by AoD Akitektuo on 29-Mar-17.
 */

public class RecipeItem {

    private String image;
    private String name;

    public RecipeItem(String image, String recipeName) {
        setImage(image);
        setName(recipeName);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
