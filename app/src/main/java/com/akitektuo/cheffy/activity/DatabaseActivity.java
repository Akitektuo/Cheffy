package com.akitektuo.cheffy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.akitektuo.cheffy.R;

public class DatabaseActivity extends Activity {

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
    }
}
