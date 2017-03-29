package com.akitektuo.cheffy.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.akitektuo.cheffy.R;
import com.akitektuo.cheffy.adapter.RecipeAdapter;
import com.akitektuo.cheffy.adapter.RecipeItem;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        RecyclerView list = (RecyclerView) findViewById(R.id.list_recipes);
        Bitmap resizedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.food);
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        ArrayList<RecipeItem> recipeItems = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            recipeItems.add(new RecipeItem(resizedBitmap, "item_" + i));
        }
        list.setAdapter(new RecipeAdapter(this, recipeItems));
        list.setLayoutManager(new LinearLayoutManager(this));
    }
}
