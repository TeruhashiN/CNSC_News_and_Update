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

                    // Hide the FloatingActionButton
                    floatingActionButton.setVisibility(View.GONE);

                    // Show the NEWS of bottom navigation bar
                    BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
                    Menu menu = bottomNavigationView.getMenu();
                    MenuItem newsItem = menu.findItem(R.id.newspage);
                    newsItem.setChecked(true);
                } else if (position == 1) {
                    // Announcement tab selected
                    // Handle the logic or UI changes for the Announcement tab

                    // Show the FloatingActionButton if user is an admin
                    if (firebaseUser.getUid().equals(ADMIN_UID)) {
                        floatingActionButton.setVisibility(View.VISIBLE);
                    } else {
                        floatingActionButton.setVisibility(View.GONE);
                    }


                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(NewsSection.this, Upload_announcement_news.class);
                            startActivity(intent);
                        }
                    });

                } else if (position == 2) {
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(NewsSection.this, upload_department_news.class);
                            startActivity(intent);
                        }
                    });



                } else if (position == 3) {
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(NewsSection.this, upload_breakthrough_news.class);
                            startActivity(intent);
                        }
                    });


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
                // Default click listener behavior for non-Announcement tab
                // Add your code here for the desired behavior
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
        // Below code will let you fix the bottom navigation bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.newspage:
                        // Handle click for the "News" tab
                        // Add your code here for the desired behavior
                        return true;
                    case R.id.searchpage:
                        // Handle click for the "Announcement" tab
                        Intent announcementIntent = new Intent(NewsSection.this, MainActivity.class);
                        startActivity(announcementIntent);
                        return true;
                    case R.id.homepage:
                        // Handle click for the "Department News" tab
                        Intent departmentIntent = new Intent(NewsSection.this, MainActivity.class);
                        startActivity(departmentIntent);
                        return true;
                    case R.id.settingspage:
                        // Handle click for the "Breakthrough" tab
                        Intent breakthroughIntent = new Intent(NewsSection.this, MainActivity.class);
                        startActivity(breakthroughIntent);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }
}
