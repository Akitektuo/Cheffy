package com.akitektuo.cheffy.activity;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
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

import static com.akitektuo.cheffy.util.Constant.CURSOR_RECIPE;
import static com.akitektuo.cheffy.util.Constant.KEY_NAME;
import static com.akitektuo.cheffy.util.Tool.setListViewHeightBasedOnItems;

public class RecipeActivity extends Activity {

    private RelativeLayout layoutTitle;
    private ImageView imageFood;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        layoutTitle = (RelativeLayout) findViewById(R.id.layout_title);
        Bitmap resizedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.shade);
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, new ByteArrayOutputStream());
        layoutTitle.setBackground(new BitmapDrawable(resizedBitmap));
        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView textTitle = (TextView) findViewById(R.id.text_title);
        imageFood = (ImageView) findViewById(R.id.image_food);
        ListView listIngredients = (ListView) findViewById(R.id.list_ingredients);
        database = new DatabaseHelper(this);
        Cursor cursor = database.getRecipeForName(getIntent().getStringExtra(KEY_NAME));
        if (cursor.moveToFirst()) {
            textTitle.setText(cursor.getString(CURSOR_RECIPE));

            ArrayList<IngredientItem> ingredients = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                ingredients.add(new IngredientItem("ingredient_" + i, ((i + 1) * 50) + " g"));
            }
            listIngredients.setAdapter(new IngredientAdapter(this, ingredients));
            if (!setListViewHeightBasedOnItems(listIngredients)) {
                finish();
            }
        }
    }
}
