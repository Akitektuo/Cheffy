package com.akitektuo.cheffy.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.akitektuo.cheffy.R;
import com.akitektuo.cheffy.adapter.RecipeAdapter;
import com.akitektuo.cheffy.adapter.RecipeItem;
import com.akitektuo.cheffy.database.DatabaseHelper;

import java.util.ArrayList;

import static com.akitektuo.cheffy.util.Tool.getBitmapForName;

public class ListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        RecyclerView list = (RecyclerView) findViewById(R.id.list_recipes);
//        Bitmap resizedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.food);
//        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        ArrayList<RecipeItem> recipeItems = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            recipeItems.add(new RecipeItem(resizedBitmap, "item_" + i));
//        }
        DatabaseHelper database = new DatabaseHelper(this);
        Cursor cursor = database.getRecipeAlphabetically();
        if (cursor.moveToFirst()) {
            do {
                recipeItems.add(new RecipeItem(getBitmapForName(this, cursor.getString(6)), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        list.setAdapter(new RecipeAdapter(this, recipeItems));
        list.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.button_database).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DatabaseActivity.class));
            }
        });
    }
}
