package com.akitektuo.cheffy.activity;

import android.Manifest;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.akitektuo.cheffy.R;
import com.akitektuo.cheffy.adapter.RecipeAdapter;
import com.akitektuo.cheffy.adapter.RecipeItem;
import com.akitektuo.cheffy.database.DatabaseHelper;
import com.akitektuo.cheffy.model.Recipe;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yalantis.pulltomakesoup.PullToRefreshView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static com.akitektuo.cheffy.util.Constant.CURSOR_INGREDIENT;
import static com.akitektuo.cheffy.util.Constant.CURSOR_PICTURE;
import static com.akitektuo.cheffy.util.Constant.CURSOR_RECIPE;
import static com.akitektuo.cheffy.util.Constant.HOST;
import static com.akitektuo.cheffy.util.Constant.keyIngredient;
import static com.akitektuo.cheffy.util.Constant.searchRequest;
import static com.akitektuo.cheffy.util.Tool.convertListToString;
import static com.akitektuo.cheffy.util.Tool.saveImage;

public class ListActivity extends Activity {
    private PullToRefreshView mPullToRefreshView;
    private RecyclerView list;
    private ArrayList<RecipeItem> recipeItems;
    private RecipeAdapter recipeAdapter;
    private AutoCompleteTextView autoEditSearch;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recipeItems = new ArrayList<>();
        autoEditSearch = (AutoCompleteTextView) findViewById(R.id.edit_auto_search);
        list = (RecyclerView) findViewById(R.id.list_recipes);
        database = new DatabaseHelper(this);
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        findViewById(R.id.button_refresh_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshList();
                autoEditSearch.setText("");
            }
        });

        ActivityCompat.requestPermissions(ListActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 3);

        list.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (autoEditSearch.getText().toString().isEmpty()) {
                    refreshList();
                } else {
                    getResultForSearch();
                }
            }
        });
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RecipesHttpRequestTask().execute();
            }
        });

        refreshList();
        setSearchSuggestions();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (searchRequest) {
            autoEditSearch.setText(keyIngredient);
            searchList(keyIngredient);
            searchRequest = false;
        }
    }

    private void refreshList() {
        Cursor cursorItems = database.getRecipeAlphabetically();
        if (cursorItems.moveToFirst()) {
            recipeItems.clear();
            do {
                recipeItems.add(new RecipeItem(cursorItems.getString(CURSOR_PICTURE), cursorItems.getString(CURSOR_RECIPE)));
            } while (cursorItems.moveToNext());
        }
        cursorItems.close();
        recipeAdapter = new RecipeAdapter(this, recipeItems);
        list.setAdapter(recipeAdapter);
    }

    private void searchList(String ingredient) {
        Cursor cursorSearch = database.getRecipeAlphabetically();
        if (cursorSearch.moveToFirst()) {
            recipeItems.clear();
            do {
                if (cursorSearch.getString(CURSOR_INGREDIENT).contains(ingredient)) {
                    recipeItems.add(new RecipeItem(cursorSearch.getString(CURSOR_PICTURE), cursorSearch.getString(CURSOR_RECIPE)));
                }
            } while (cursorSearch.moveToNext());
        }
        cursorSearch.close();
        recipeAdapter = new RecipeAdapter(this, recipeItems);
        list.setAdapter(recipeAdapter);
    }

    private void setSearchSuggestions() {
        ArrayList<String> recipesNames = new ArrayList<>();
        Cursor cursorRecipes = database.getRecipe();
        if (cursorRecipes.moveToFirst()) {
            do {
                recipesNames.add(cursorRecipes.getString(CURSOR_RECIPE));
            } while (cursorRecipes.moveToNext());
        }
        cursorRecipes.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recipesNames);
        autoEditSearch.setAdapter(adapter);
    }

    private void getResultForSearch() {
        if (!autoEditSearch.getText().toString().isEmpty()) {
            Cursor cursorSearch = database.getRecipeForName(autoEditSearch.getText().toString());
            if (cursorSearch.moveToFirst()) {
                recipeItems.clear();
                recipeItems.add(new RecipeItem(cursorSearch.getString(CURSOR_PICTURE), cursorSearch.getString(CURSOR_RECIPE)));
                recipeAdapter = new RecipeAdapter(getApplicationContext(), recipeItems);
                list.setAdapter(recipeAdapter);
            }
            cursorSearch.close();
        } else {
            onResume();
        }
    }
    private void persist(Recipe recipe) {
        Log.d("Persists", "Added recipe: " + recipe.getName() + " to database");
        database.addRecipe(recipe.getId(), recipe.getName(), recipe.getContent(),
                convertListToString(recipe.getIngredients()), convertListToString(recipe.getWeights()),
                recipe.getDuration(), recipe.getPicture());
    }

    private void storeImage(Bitmap image, String name) {
        Log.d("Saves", "Saved image: " + name);
        // TODO: 07.04.2017 add as parameter imageHd
//         saveImage(this, image, imageHd, name);
        saveImage(this, image, name);
    }

    private class RecipesHttpRequestTask extends AsyncTask<Void, Void, Recipe[]> {

        @Override
        protected Recipe[] doInBackground(Void... params) {
            final String url = HOST + "/recipes/all";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Object> entity = new HttpEntity<>(headers);
            try {
                ResponseEntity<Recipe[]> out = restTemplate.exchange(url, HttpMethod.GET, entity, Recipe[].class);
                if (out.getStatusCode() == HttpStatus.OK)
                    return out.getBody();
                else
                    Toast.makeText(getApplicationContext(), "Code "+out.getStatusCode(), Toast.LENGTH_SHORT).show();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Recipe[] recipes) {
            if(recipes!=null) {
                Log.d("Result", "API returned " + recipes.length + " items");
                database.deleteDatabase();
                for (final Recipe recipe : recipes) {
                    persist(recipe);
                    Target t = new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            storeImage(bitmap, recipe.getPicture());
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            Toast.makeText(getApplicationContext(), "Failed to download image", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                            Log.d("onPrepareLoad", "Preparing");
                        }
                    };
                    Picasso.with(getApplicationContext())
                            .load(HOST + "/assets/" + recipe.getPicture())
                            .into(t);
                }
                refreshList();
                mPullToRefreshView.setRefreshing(false);
            } else {
                Toast.makeText(getApplicationContext(), "A network error occurred", Toast.LENGTH_SHORT).show();
                mPullToRefreshView.setRefreshing(false);
            }
        }
    }
}
