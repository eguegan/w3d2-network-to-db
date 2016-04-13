package com.example.admin.w3d2networktodatabase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 4/13/2016.
 */
public class SaveToDbTask extends AsyncTask {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "TAG_";
    private OkHttpClient client = new OkHttpClient();
    private MainActivity mMainActivity;

    public SaveToDbTask(MainActivity mMa){
        mMainActivity = mMa;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        String getResponse;
        try {
            getResponse = doGetRequest("http://api.duckduckgo.com/?q=simpsons+characters&format=json");
            Log.d("TAG", "doInBackground: " + getResponse);
            parseJson(getResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String doGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public void parseJson(String rawJson){
        String charInfo;
        String charImg;
        try {
            JSONObject jsonObject = new JSONObject(rawJson);
            JSONArray jsonArray = jsonObject.getJSONArray("RelatedTopics");

            CharactersDatabaseHelper charactersDatabaseHelper = new CharactersDatabaseHelper(mMainActivity);
            SQLiteDatabase db = charactersDatabaseHelper.getWritableDatabase();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject x = jsonArray.getJSONObject(i);
                charInfo = x.getString("Text");
                JSONObject icon = x.getJSONObject("Icon");
                charImg = icon.getString("URL");

                fillDatabase(db, charImg, charInfo);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        logInformation();
    }

    public void fillDatabase(SQLiteDatabase db, String imgUrl, String charDescription) {


        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(CharactersDatabaseHelper.KEY_IMG_URL, imgUrl);
            values.put(CharactersDatabaseHelper.KEY_DESCRIPTION, charDescription);

            db.insertOrThrow(CharactersDatabaseHelper.TABLE_CHARACTERS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public void logInformation() {
        CharactersDatabaseHelper charactersDatabaseHelper = new CharactersDatabaseHelper(mMainActivity);

        final String POSTS_SELECT_QUERY = "SELECT * FROM characters";

        SQLiteDatabase db = charactersDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    String characterImg = cursor.getString(cursor.getColumnIndex(CharactersDatabaseHelper.KEY_IMG_URL));
                    String characterDescription = cursor.getString(cursor.getColumnIndex(CharactersDatabaseHelper.KEY_DESCRIPTION));

                    Log.d(TAG, "Character Image Url: " + characterImg);
                    Log.d(TAG, "Character Description: " + characterDescription);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }
}
