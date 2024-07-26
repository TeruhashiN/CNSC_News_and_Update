package com.bee.cnscnewsandupdate.users_ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.bee.cnscnewsandupdate.Announcement_data.PageAdapter;
import com.bee.cnscnewsandupdate.R;
import com.bee.cnscnewsandupdate.uploading_data.Upload_announcement_news;
import com.bee.cnscnewsandupdate.uploading_data.upload_breakthrough_news;
import com.bee.cnscnewsandupdate.uploading_data.upload_department_news;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NewsSection extends AppCompatActivity {

    private static final String ADMIN_UID = "i1R5VSXkGcS7OkV4VCJlUDPViPz1"; // admin code

    private String[] tabs = {"Today's News", "Announcement", "Department News", "Breakthrough"};
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private PageAdapter adapter;

    private FirebaseAuth authProfile;


    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_section);


        // Show the NEWS of bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem newsItem = menu.findItem(R.id.newspage);
        newsItem.setChecked(true);

    }
}
