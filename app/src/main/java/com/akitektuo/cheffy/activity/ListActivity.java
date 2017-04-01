package com.akitektuo.cheffy.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.akitektuo.cheffy.R;
import com.akitektuo.cheffy.adapter.RecipeAdapter;
import com.akitektuo.cheffy.adapter.RecipeItem;
import com.akitektuo.cheffy.database.DatabaseHelper;

import java.util.ArrayList;

import static com.akitektuo.cheffy.util.Constant.CURSOR_PICTURE;
import static com.akitektuo.cheffy.util.Constant.CURSOR_RECIPE;
import static com.akitektuo.cheffy.util.Tool.getBitmapForName;

public class ListActivity extends Activity {

    private AutoCompleteTextView autoEditSearch;
    private RecyclerView list;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        /**
         * This will go away, just for testing...
         */

        autoEditSearch = (AutoCompleteTextView) findViewById(R.id.edit_auto_search);
        list = (RecyclerView) findViewById(R.id.list_recipes);
        database = new DatabaseHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<RecipeItem> recipeItems = new ArrayList<>();
        Cursor cursorItems = database.getRecipeAlphabetically();
        if (cursorItems.moveToFirst()) {
            do {
                recipeItems.add(new RecipeItem(getBitmapForName(this, cursorItems.getString(CURSOR_PICTURE)), cursorItems.getString(CURSOR_RECIPE)));
            } while (cursorItems.moveToNext());
        }
        cursorItems.close();
        list.setAdapter(new RecipeAdapter(this, recipeItems));
        list.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.button_database).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DatabaseActivity.class));
            }
        });
        ArrayList<String> recipes = new ArrayList<>();
        Cursor cursorRecipes = database.getRecipe();
        if (cursorRecipes.moveToFirst()) {
            do {
                recipes.add(cursorRecipes.getString(CURSOR_RECIPE));
            } while (cursorRecipes.moveToNext());
        }
        cursorRecipes.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recipes);
        autoEditSearch.setAdapter(adapter);
        findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!autoEditSearch.getText().toString().isEmpty()) {
                    Cursor cursorSearch = database.getRecipeForName(autoEditSearch.getText().toString());
                    if (cursorSearch.moveToFirst()) {
                        ArrayList<RecipeItem> recipeSearch = new ArrayList<>();
                        recipeSearch.add(new RecipeItem(getBitmapForName(getApplicationContext(), cursorSearch.getString(CURSOR_PICTURE)), cursorSearch.getString(CURSOR_RECIPE)));
                        list.setAdapter(new RecipeAdapter(getApplicationContext(), recipeSearch));
                    }
                    cursorSearch.close();
                } else {
                    onResume();
                }
            }
        });
    }
}
