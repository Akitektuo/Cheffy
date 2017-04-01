package com.akitektuo.cheffy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.akitektuo.cheffy.R;
import com.akitektuo.cheffy.database.DatabaseHelper;

public class DatabaseActivity extends Activity {

    private EditText editName;
    private EditText editDescription;
    private EditText editIngredients;
    private EditText editQuantity;
    private EditText editDuration;
    private EditText editPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        findViewById(R.id.button_database_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editName = (EditText) findViewById(R.id.edit_name);
        editDescription = (EditText) findViewById(R.id.edit_description);
        editIngredients = (EditText) findViewById(R.id.edit_ingredients);
        editQuantity = (EditText) findViewById(R.id.edit_quantity);
        editDuration = (EditText) findViewById(R.id.edit_duration);
        editPicture = (EditText) findViewById(R.id.edit_picture);
        final DatabaseHelper database = new DatabaseHelper(this);
        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.addRecipe(database.getRecipeId() + 1, editName.getText().toString(),
                        editDescription.getText().toString(), editIngredients.getText().toString(),
                        editQuantity.getText().toString(), Long.parseLong(editDuration.getText().toString()),
                        editPicture.getText().toString());
                editName.setText("");
                editDescription.setText("");
                editIngredients.setText("");
                editQuantity.setText("");
                editDuration.setText("");
                editPicture.setText("");
            }
        });
    }
}
