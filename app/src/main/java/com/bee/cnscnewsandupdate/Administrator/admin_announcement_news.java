package com.bee.cnscnewsandupdate.Administrator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bee.cnscnewsandupdate.DataClass;
import com.bee.cnscnewsandupdate.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.ArrayList;

//32 mins


public class admin_announcement_news extends Fragment {

    RecyclerView recyclerView;
    List<DataClass> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;



    // below is not needed
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    // upper not needed

    public admin_announcement_news() {

    }
    public static admin_announcement_news newInstance(String param1, String param2) {
        admin_announcement_news fragment = new admin_announcement_news();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
//
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(admin_announcement_news.this, 0);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_announcement_news, container, false);

    }
}