package com.bee.cnscnewsandupdate.users_ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bee.cnscnewsandupdate.R;
import com.bee.cnscnewsandupdate.ScholarShipRecyclerAdapter;
import com.bee.cnscnewsandupdate.Scholarship;
import com.bee.cnscnewsandupdate.ScholarshipActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScholarshipsFragment extends Fragment {
    RecyclerView recyclerView;

    SearchView searchView;
    private List<Scholarship> scholarships = new ArrayList<>();
    private List<String> scholarshipIds = new ArrayList<>();
    private Context context;
    private ScholarShipRecyclerAdapter adapter;
    private Gson gson;

    public ScholarshipsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scholarships, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gson = new Gson();
        adapter = new ScholarShipRecyclerAdapter(scholarships);
        recyclerView = view.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ScholarShipRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(Scholarship scholarship) {
                final Intent i = new Intent(context, ScholarshipActivity.class);
                i.putExtra("scholarship", gson.toJson(scholarship));
                startActivity(i);
            }
        });

        final FirebaseApp secondary = FirebaseApp.getInstance("educasst");
        final  FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance(secondary);
        firebaseFirestore.collection("Scholarships").whereGreaterThan("budget", 0)
                .orderBy("createdAt", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            final DocumentSnapshot doc = dc.getDocument();
                            final Scholarship scholarship = doc.toObject(Scholarship.class).withId(doc.getId());
                            final int index = scholarshipIds.indexOf(scholarship.docid);
                            if (scholarship.getCreatedAt() == null)  {
                                scholarship.setCreatedAt(new Date());
                            }
                            if (scholarship.getDeclined() == null)  {
                                scholarship.setDeadline(new Date());
                            }

                            switch (dc.getType()) {
                                case ADDED:
                                    scholarships.add(scholarship);
                                    scholarshipIds.add( scholarship.docid);
                                    adapter.notifyItemInserted(scholarships.size() - 1);
                                    break;
                                case MODIFIED:
                                    if (index > -1) {
                                        scholarships.set(index, scholarship);
                                        adapter.notifyItemChanged(index);
                                    }
                                    break;
                                case REMOVED:
                                    break;
                            }
                        }
                    }
                });
    }
}
