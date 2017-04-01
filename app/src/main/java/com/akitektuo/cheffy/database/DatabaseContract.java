package com.akitektuo.cheffy.database;

import android.provider.BaseColumns;

/**
 * Created by AoD Akitektuo on 01-Apr-17.
 */

class DatabaseContract {
    abstract class RecipeContractEntry implements BaseColumns {
        static final String TABLE_NAME = "recipe";
        static final String COLUMN_ID = "id";
        static final String COLUMN_NAME = "name";
        static final String COLUMN_DESCRIPTION = "description";
        static final String COLUMN_INGREDIENT = "ingredient";
        static final String COLUMN_QUANTITY = "quantity";
        static final String COLUMN_DURATION = "duration";
        static final String COLUMN_PICTURE = "picture";
    }
}
