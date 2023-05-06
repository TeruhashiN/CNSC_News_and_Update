package com.bee.cnscnewsandupdate.users_ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bee.cnscnewsandupdate.R;
import com.bee.cnscnewsandupdate.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.homepage:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.searchpage:
                    replaceFragment(new SearchFragment());
                    break;
                case R.id.newspage:
                    Intent news_intent = new Intent(this, NewsSection.class);
                    startActivity(news_intent);
                    break;
                case R.id.settingspage:
                    replaceFragment(new SettingsFragment());

                    break;



            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }


    public ActionBar getMyActionBar() {
        return getSupportActionBar();
    }
}