package com.bee.cnscnewsandupdate.users_ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bee.cnscnewsandupdate.Announcement_data.DataClass;
import com.bee.cnscnewsandupdate.Announcement_data.MyAdapter;
import com.bee.cnscnewsandupdate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;
import java.util.ArrayList;


public class todays_news extends Fragment {
    RecyclerView recyclerView;
    List<DataClass> dataList;
    List<DatabaseReference> databaseReferences;
    ValueEventListener eventListener;

    SearchView searchView;
    MyAdapter adapter;

    public todays_news() {

    }

    public static todays_news newInstance(String param1, String param2) {
        todays_news fragment = new todays_news();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todays_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Click listener for the ImageView
        ImageView searchIcon = view.findViewById(R.id.searchIcon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView.getVisibility() == View.VISIBLE) {
                    searchView.setVisibility(View.GONE);
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                } else {
                    searchView.setVisibility(View.VISIBLE);
                    searchView.requestFocus();
                }
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        searchView = view.findViewById(R.id.search);
        if (searchView != null) {
            searchView.clearFocus();
        }

        recyclerView = view.findViewById(R.id.recyclerView);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();
        adapter = new MyAdapter(getContext(), dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        DatabaseReference breakthroughNewsRef = FirebaseDatabase.getInstance().getReference("Breakthrough News");
        DatabaseReference announcementNewsRef = FirebaseDatabase.getInstance().getReference("Announcement News");
        DatabaseReference departmentNewsRef = FirebaseDatabase.getInstance().getReference("Department News");

        databaseReferences = new ArrayList<>();
        databaseReferences.add(breakthroughNewsRef);
        databaseReferences.add(announcementNewsRef);
        databaseReferences.add(departmentNewsRef);

        for (DatabaseReference reference : databaseReferences) {
            eventListener = reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                        dataClass.setKey(itemSnapshot.getKey());
                        dataList.add(dataClass);
                    }
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    dialog.dismiss();
                }
            });
        }

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchList(newText);
                    return true;
                }
            });
        }
    }

    public void searchList(String text) {
        ArrayList<DataClass> searchList = new ArrayList<>();
        for (DataClass dataClass: dataList) {
            if (dataClass.getDataTitle().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }
        }
        adapter.searchDateList(searchList);
    }
}
