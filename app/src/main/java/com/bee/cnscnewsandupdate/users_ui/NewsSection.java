package com.bee.cnscnewsandupdate.users_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.bee.cnscnewsandupdate.Announcement_data.PageAdapter;
import com.bee.cnscnewsandupdate.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class NewsSection extends AppCompatActivity {

    private String[] tabs = {"Today's News", "Announcement", "Department News", "Breakthrough"};
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private PageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_section);

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
    }
}
