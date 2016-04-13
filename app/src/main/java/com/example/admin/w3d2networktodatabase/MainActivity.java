package com.example.admin.w3d2networktodatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SaveToDbTask saveToDbTask = new SaveToDbTask(this);
        saveToDbTask.execute();
    }
}
