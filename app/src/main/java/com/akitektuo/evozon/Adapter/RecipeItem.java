package com.akitektuo.evozon.Adapter;

import android.graphics.Bitmap;

/**
 * Created by AoD Akitektuo on 29-Mar-17.
 */

public class RecipeItem {

    private Bitmap bitmap;
    private String name;

    public RecipeItem(Bitmap image, String recipeName) {
        setBitmap(image);
        setName(recipeName);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
