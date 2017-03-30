package com.akitektuo.cheffy.activity;

import android.app.Activity;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.akitektuo.cheffy.util.Constant.KEY_NAME;
import static com.akitektuo.cheffy.util.Constant.setListViewHeightBasedOnItems;

public class RecipeActivity extends Activity {

    RelativeLayout layoutTitle;
    ImageView imageFood;

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
        textTitle.setText(getIntent().getStringExtra(KEY_NAME));
        imageFood = (ImageView) findViewById(R.id.image_food);
        ListView listIngredients = (ListView) findViewById(R.id.list_ingredients);
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
