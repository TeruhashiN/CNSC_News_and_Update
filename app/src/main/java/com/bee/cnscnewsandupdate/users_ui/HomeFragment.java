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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.bee.cnscnewsandupdate.Announcement_data.DataClass;
import com.bee.cnscnewsandupdate.Announcement_data.MyAdapter;
import com.bee.cnscnewsandupdate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final int MAX_ITEMS = 4;

    List<DatabaseReference> databaseReferences;
    RecyclerView recyclerView, recyclerViews;
    List<DataClass> dataList;
    List<DataClass> dataListLimited; // List for limited items
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    SearchView searchView;
    MyAdapter adapter;
    MyAdapter limitedAdapter; // Adapter for limited items

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerViews = view.findViewById(R.id.recyclerViews);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerViews.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();
        dataListLimited = new ArrayList<>();
        adapter = new MyAdapter(getContext(), dataList);
        limitedAdapter = new MyAdapter(getContext(), dataListLimited);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerViews.setAdapter(limitedAdapter);
        recyclerViews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        databaseReference = FirebaseDatabase.getInstance().getReference("Announcement News");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);

                    // Limit the number of items
                    if (dataListLimited.size() < MAX_ITEMS) {
                        dataListLimited.add(dataClass);
                    }
                }
                adapter.notifyDataSetChanged();
                limitedAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

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

                        // Limit the number of items
                        if (dataListLimited.size() < MAX_ITEMS) {
                            dataListLimited.add(dataClass);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    limitedAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    dialog.dismiss();
                }
            });
        }
    }
}
