package com.akitektuo.cheffy.activity;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akitektuo.cheffy.R;
import com.akitektuo.cheffy.adapter.IngredientAdapter;
import com.akitektuo.cheffy.adapter.IngredientItem;
import com.akitektuo.cheffy.database.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.akitektuo.cheffy.util.Constant.CURSOR_DESCRIPTION;
import static com.akitektuo.cheffy.util.Constant.CURSOR_DURATION;
import static com.akitektuo.cheffy.util.Constant.CURSOR_INGREDIENT;
import static com.akitektuo.cheffy.util.Constant.CURSOR_PICTURE;
import static com.akitektuo.cheffy.util.Constant.CURSOR_QUANTITY;
import static com.akitektuo.cheffy.util.Constant.CURSOR_RECIPE;
import static com.akitektuo.cheffy.util.Constant.KEY_NAME;
import static com.akitektuo.cheffy.util.Constant.keyIngredient;
import static com.akitektuo.cheffy.util.Constant.searchRequest;
import static com.akitektuo.cheffy.util.Tool.convertStringToList;
import static com.akitektuo.cheffy.util.Tool.getImage;
import static com.akitektuo.cheffy.util.Tool.setListViewHeightBasedOnItems;

public class RecipeActivity extends Activity {

    private TextView textTitle;
    private TextView textDescription;
    private ImageView imageFood;
    private TextView textDuration;
    private ListView listIngredients;
    private ArrayList<IngredientItem> ingredientItems;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        RelativeLayout layoutTitle = (RelativeLayout) findViewById(R.id.layout_title);
        Bitmap resizedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.shade);
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, new ByteArrayOutputStream());
        layoutTitle.setBackground(new BitmapDrawable(resizedBitmap));
        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        textTitle = (TextView) findViewById(R.id.text_title);
        imageFood = (ImageView) findViewById(R.id.image_food);
        listIngredients = (ListView) findViewById(R.id.list_ingredients);
        textDescription = (TextView) findViewById(R.id.text_recipe);
        textDuration = (TextView) findViewById(R.id.text_time);
        ingredientItems = new ArrayList<>();
        database = new DatabaseHelper(this);
        listIngredients.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchRequest = true;
                keyIngredient = ingredientItems.get(i).toString();
                finish();
                return true;
            }
        });
        setInformation();
    }

    private void setInformation() {
        Cursor cursor = database.getRecipeForName(getIntent().getStringExtra(KEY_NAME));
        if (cursor.moveToFirst()) {
            textTitle.setText(cursor.getString(CURSOR_RECIPE));
            textDescription.setText(cursor.getString(CURSOR_DESCRIPTION));
            ArrayList<String> ingredients = convertStringToList(cursor.getString(CURSOR_INGREDIENT));
            ArrayList<String> quantities = convertStringToList(cursor.getString(CURSOR_QUANTITY));
            for (int i = 0; i < ingredients.size(); i++) {
                ingredientItems.add(new IngredientItem(ingredients.get(i), quantities.get(i)));
            }
            textDuration.setText(convertMillisToTime(cursor.getLong(CURSOR_DURATION)));
            imageFood.setImageBitmap(getImage(this, cursor.getString(CURSOR_PICTURE)));
            cursor.close();
        } else {
            for (int i = 0; i < 11; i++) {
                ingredientItems.add(new IngredientItem("ingredient_" + i, ((i + 1) * 50) + " g"));
            }
        }
        listIngredients.setAdapter(new IngredientAdapter(this, ingredientItems));
        if (!setListViewHeightBasedOnItems(listIngredients)) {
            finish();
        }
    }

    private String convertMillisToTime(long time) {
        int seconds = (int) time / 1000, minutes = 0, hours = 0;
        while (seconds > 59) {
            minutes++;
            seconds -= 60;
        }
        while (minutes > 59) {
            hours++;
            minutes -= 60;
        }
        String sec = "00", min = "00", h = "00";
        if (seconds != 0) {
            sec = String.valueOf(seconds);
            if (sec.length() == 1) {
                sec = "0" + sec;
            }
        }
        if (minutes != 0) {
            min = String.valueOf(minutes);
            if (min.length() == 1) {
                min = "0" + min;
            }
        }
        if (hours != 0) {
            h = String.valueOf(hours);
            if (h.length() == 1) {
                h = "0" + h;
            }
        }
        return h + ":" + min + ":" + sec;
    }
}
