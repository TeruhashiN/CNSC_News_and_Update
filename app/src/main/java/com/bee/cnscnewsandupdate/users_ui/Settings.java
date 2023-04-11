package com.bee.cnscnewsandupdate.users_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.bee.cnscnewsandupdate.R;
import com.bee.cnscnewsandupdate.login_and_register.loginregister;

public class Settings extends AppCompatActivity {
    ImageButton homepage_button, login_image_button;

    TextView login_text_button;
    Switch dark_mode_switch;
    boolean nightMODE;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dark_mode_switch = findViewById(R.id.darkmode_switch);

        // used SharedPreferences to save mode if exit the app and go back again
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMODE = sharedPreferences.getBoolean("night", false); // Light mode is the default mode

        if (nightMODE) {
            dark_mode_switch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }


        dark_mode_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nightMODE) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor =  sharedPreferences.edit();
                    editor.putBoolean("night", true);
                }
                editor.apply();
            }
        });
        login_image_button = (ImageButton) findViewById(R.id.profile_login_button);
        login_text_button = (TextView) findViewById(R.id.login_text);


    }
    public void onclickhomepage(View view) {
        Intent homepageintent = new Intent(this, MainActivity.class);
        startActivity(homepageintent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left); // di pa to tapos

    }

    public void onclicknews(View view) {
        Intent news_intent = new Intent(this, NewsSection.class);
        startActivity(news_intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    public void onclickloginimage(View view) {
        Intent loginimage = new Intent (this, loginregister.class);
        startActivity(loginimage);
    }
    public void onclicklogintext(View view) {
        Intent logintext = new Intent (this, loginregister.class);
        startActivity(logintext);
    }
}