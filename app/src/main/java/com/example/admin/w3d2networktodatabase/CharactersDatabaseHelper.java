package com.example.admin.w3d2networktodatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 4/13/2016.
 */
public class CharactersDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "simpsons";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CHARACTERS = "characters";

    public static final String KEY_USER_ID = "_id";
    public static final String KEY_IMG_URL = "img_url";
    public static final String KEY_DESCRIPTION = "description";

    public CharactersDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_CHARACTERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_IMG_URL + " TEXT," +
                KEY_DESCRIPTION + " TEXT" +
                ")";

        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHARACTERS);
        onCreate(db);
    }
}
