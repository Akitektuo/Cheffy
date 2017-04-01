package com.akitektuo.cheffy.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.akitektuo.cheffy.R;
import com.akitektuo.cheffy.adapter.RecipeAdapter;
import com.akitektuo.cheffy.adapter.RecipeItem;
import com.akitektuo.cheffy.database.DatabaseHelper;
import com.akitektuo.cheffy.model.Recipe;
import com.akitektuo.cheffy.util.BasicImageDownloader;
import com.yalantis.pulltomakesoup.PullToRefreshView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.akitektuo.cheffy.util.Constant.CURSOR_PICTURE;
import static com.akitektuo.cheffy.util.Constant.CURSOR_RECIPE;
import static com.akitektuo.cheffy.util.Tool.getBitmapForName;

public class ListActivity extends Activity {

    private PullToRefreshView mPullToRefreshView;
    private Bitmap resizedBitmap;//aici e o buba temporara
    private RecyclerView list;
    private ArrayList<RecipeItem> recipeItems;
    private RecipeAdapter recipeAdapter;
    private AutoCompleteTextView autoEditSearch;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        autoEditSearch = (AutoCompleteTextView) findViewById(R.id.edit_auto_search);
        list = (RecyclerView) findViewById(R.id.list_recipes);
        database = new DatabaseHelper(this);
        resizedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.food);
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        list.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.button_database).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DatabaseActivity.class));
            }
        });
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RecipesHttpRequestTask().execute();
            }
        });
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

    private class RecipesHttpRequestTask extends AsyncTask<Void, Void, Recipe[]> {
        @Override
        protected Recipe[] doInBackground(Void... params) {
            Looper.prepare();
            try {
                final String url = "https://dummy-api-ioansiran.c9users.io/recipes/all";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                HttpHeaders headers = new HttpHeaders();
                HttpEntity<Object> entity = new HttpEntity<Object>(headers);
                ResponseEntity<Recipe[]> out = restTemplate.exchange(url, HttpMethod.GET, entity, Recipe[].class);
                if(out.getStatusCode()== HttpStatus.OK){
                    Recipe[] recipes = out.getBody();
                    return recipes;
                } else return null;
            } catch (final Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Recipe[] recipes) {
            mPullToRefreshView.setRefreshing(false);//hide refresh
            if(recipes!=null) {
                recipeItems.clear();
                for (final Recipe r : recipes){
                    BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
                        @Override
                        public void onError(BasicImageDownloader.ImageError error) {
                        }
                        @Override
                        public void onProgressChange(int percent) {
                        }
                        @Override
                        public void onComplete(Bitmap result) {
                            resizedBitmap = result;
                            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
                            recipeItems.add(new RecipeItem(resizedBitmap, r.getName()));
                            recipeAdapter.notifyDataSetChanged();
                        }
                    });
                    downloader.download("https://dummy-api-ioansiran.c9users.io/assets/"+r.getPicture(),false);
                }
            } else
                Toast.makeText(getApplicationContext(),"A network error occurred", Toast.LENGTH_LONG).show();
        }

    }

}
