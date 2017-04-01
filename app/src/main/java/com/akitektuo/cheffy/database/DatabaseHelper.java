package com.akitektuo.cheffy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by AoD Akitektuo on 01-Apr-17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_QUERY = "CREATE TABLE " + DatabaseContract.RecipeContractEntry.TABLE_NAME + " (" +
            DatabaseContract.RecipeContractEntry.COLUMN_ID + " NUMBER," +
            DatabaseContract.RecipeContractEntry.COLUMN_NAME + " TEXT," +
            DatabaseContract.RecipeContractEntry.COLUMN_DESCRIPTION + " TEXT," +
            DatabaseContract.RecipeContractEntry.COLUMN_INGREDIENT + " TEXT," +
            DatabaseContract.RecipeContractEntry.COLUMN_QUANTITY + " TEXT," +
            DatabaseContract.RecipeContractEntry.COLUMN_DURATION + " INTEGER," +
            DatabaseContract.RecipeContractEntry.COLUMN_PICTURE + " TEXT" + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addRecipe(int id, String name, String description, String ingredient, String quantity, long duration, String picture) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.RecipeContractEntry.COLUMN_ID, id);
        contentValues.put(DatabaseContract.RecipeContractEntry.COLUMN_NAME, name);
        contentValues.put(DatabaseContract.RecipeContractEntry.COLUMN_DESCRIPTION, description);
        contentValues.put(DatabaseContract.RecipeContractEntry.COLUMN_INGREDIENT, ingredient);
        contentValues.put(DatabaseContract.RecipeContractEntry.COLUMN_QUANTITY, quantity);
        contentValues.put(DatabaseContract.RecipeContractEntry.COLUMN_DURATION, duration);
        contentValues.put(DatabaseContract.RecipeContractEntry.COLUMN_PICTURE, picture);
        getWritableDatabase().insert(DatabaseContract.RecipeContractEntry.TABLE_NAME, null, contentValues);
    }

    public Cursor getRecipe() {
        String[] list = {DatabaseContract.RecipeContractEntry.COLUMN_ID,
                DatabaseContract.RecipeContractEntry.COLUMN_NAME,
                DatabaseContract.RecipeContractEntry.COLUMN_DESCRIPTION,
                DatabaseContract.RecipeContractEntry.COLUMN_INGREDIENT,
                DatabaseContract.RecipeContractEntry.COLUMN_QUANTITY,
                DatabaseContract.RecipeContractEntry.COLUMN_DURATION,
                DatabaseContract.RecipeContractEntry.COLUMN_PICTURE};
        return getReadableDatabase().query(DatabaseContract.RecipeContractEntry.TABLE_NAME, list, null, null, null, null, null);
    }

    public Cursor getRecipeAlphabetically() {
        String[] list = {DatabaseContract.RecipeContractEntry.COLUMN_ID,
                DatabaseContract.RecipeContractEntry.COLUMN_NAME,
                DatabaseContract.RecipeContractEntry.COLUMN_DESCRIPTION,
                DatabaseContract.RecipeContractEntry.COLUMN_INGREDIENT,
                DatabaseContract.RecipeContractEntry.COLUMN_QUANTITY,
                DatabaseContract.RecipeContractEntry.COLUMN_DURATION,
                DatabaseContract.RecipeContractEntry.COLUMN_PICTURE};
        return getReadableDatabase().query(DatabaseContract.RecipeContractEntry.TABLE_NAME, list,
                null, null, null, null, DatabaseContract.RecipeContractEntry.COLUMN_NAME + " COLLATE NOCASE ASC");
    }

    public int getRecipeId() {
        int id = -1;
        Cursor cursor = getRecipe();
        if (cursor.moveToLast()) {
            id = cursor.getInt(0);
        }
        return id;
    }

    public Cursor getRecipeForName(String name) {
        String[] results = {DatabaseContract.RecipeContractEntry.COLUMN_ID,
                DatabaseContract.RecipeContractEntry.COLUMN_NAME,
                DatabaseContract.RecipeContractEntry.COLUMN_DESCRIPTION,
                DatabaseContract.RecipeContractEntry.COLUMN_INGREDIENT,
                DatabaseContract.RecipeContractEntry.COLUMN_QUANTITY,
                DatabaseContract.RecipeContractEntry.COLUMN_DURATION,
                DatabaseContract.RecipeContractEntry.COLUMN_PICTURE};
        String selection = DatabaseContract.RecipeContractEntry.COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = {name};
        return getReadableDatabase().query(DatabaseContract.RecipeContractEntry.TABLE_NAME, results, selection, selectionArgs, null, null, null);
    }

}
