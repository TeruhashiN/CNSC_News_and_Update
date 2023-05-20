package com.bee.cnscnewsandupdate.users_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.bee.cnscnewsandupdate.Announcement_data.PageAdapter;
import com.bee.cnscnewsandupdate.R;
import com.bee.cnscnewsandupdate.uploading_data.Upload_announcement_news;
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

        authProfile = FirebaseAuth.getInstance(); // initialize authProfile
        FirebaseUser firebaseUser = authProfile.getCurrentUser();


        FloatingActionButton floatingActionButton = findViewById(R.id.fab);

        if (firebaseUser.getUid().equals(ADMIN_UID)) {
            // Show the FloatingActionButton if user is an admin
            floatingActionButton.setVisibility(View.VISIBLE);
            Log.d("DetailActivity", "FloatingActionButton object retrieved successfully");

        } else {
            // Hide the FloatingActionButton if user is not an admin
            floatingActionButton.setVisibility(View.GONE);
            Log.d("DetailActivity", "FloatingActionButton object retrieved successfully");

        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label3));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label4));

        viewPager = findViewById(R.id.pager);
        adapter = new PageAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);

        adapter.addFragment(new todays_news());
        adapter.addFragment(new announcement_news());
        adapter.addFragment(new department_news());
        adapter.addFragment(new breakthrough_news());

        viewPager.setAdapter(adapter);


        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabs[position])).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    // Today's News tab selected
                    // Handle the logic or UI changes for the Today's News tab
                } else if (position == 1) {
                    // Announcement tab selected
                    // Handle the logic or UI changes for the Announcement tab
                } else if (position == 2) {
                    // Department News tab selected
                    // Handle the logic or UI changes for the Department News tab
                } else if (position == 3) {
                    // Breakthrough tab selected
                    // Handle the logic or UI changes for the Breakthrough tab
                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do nothing
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsSection.this, Upload_announcement_news.class);
                startActivity(intent);
            }
        });
        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ObjectAnimator.ofFloat(fab, "alpha", 1.0f).start();
                        return false;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        ObjectAnimator.ofFloat(fab, "alpha", 0.5f).start();
                        return false;
                }
                return false;
            }
        });
    }
}
