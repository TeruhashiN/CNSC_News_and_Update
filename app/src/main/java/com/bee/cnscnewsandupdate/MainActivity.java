package com.bee.cnscnewsandupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;




public class MainActivity extends AppCompatActivity {
    ImageButton button_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_settings = (ImageButton) findViewById(R.id.setting_button);
    }

    public void onclicksettings(View view) {
        Intent setting_intent = new Intent(this, Settings.class);
        startActivity(setting_intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

    }
    public void onclicknews(View view) {
        Intent news_intent = new Intent(this, NewsSection.class);
        startActivity(news_intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
    public void onclicksearch(View view) {
        Intent search_intent = new Intent(this, Settings.class);
        startActivity(search_intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }


}