package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.annotation.SuppressLint;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BattleFieldView bfv = findViewById(R.id.battlefield);

        bfv.AddImage(getDrawable(R.drawable.carte_bleue));
        bfv.AddImage(getDrawable(R.drawable.carte_rouge));
        bfv.AddImage(getDrawable(R.drawable.carte_verte));
        bfv.AddImage(getDrawable(R.drawable.carte_bleue));
        bfv.AddImage(getDrawable(R.drawable.carte_rouge));
        bfv.AddImage(getDrawable(R.drawable.carte_verte));
        bfv.AddImage(getDrawable(R.drawable.carte_verte));
        bfv.AddImage(getDrawable(R.drawable.carte_verte));
        bfv.AddImage(getDrawable(R.drawable.carte_verte));
    }
}