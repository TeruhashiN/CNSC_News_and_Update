package com.bee.cnscnewsandupdate.Announcement_data;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bee.cnscnewsandupdate.Administrator.admin_NewsSection;
import com.bee.cnscnewsandupdate.R;
import com.bee.cnscnewsandupdate.users_ui.MainActivity;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailDate;
    ImageView detailImage;
    FloatingActionButton deleteButton;
    String key = "";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // gonna use this later

//        // Check if user is an admin
//        String userId = "00oGDrHUDjNV8FptXWQ4SwEA8qX2"; // Replace with actual user ID
//        boolean isAdmin = userId.equals("00oGDrHUDjNV8FptXWQ4SwEA8qX2");
//
//        // Get reference to the RelativeLayout
//        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
//
//
//        if (isAdmin) {
//            // Show the RelativeLayout if user is not an admin
//            relativeLayout.setVisibility(View.VISIBLE);
//            Log.d("DetailActivity", "RelativeLayout object retrieved successfully");
//
//        } else {
//            // Hide the RelativeLayout if user is an admin
//            relativeLayout.setVisibility(View.GONE);
//            Log.d("DetailActivity", "RelativeLayout object retrieved successfully");
//
//        }


        detailDesc = findViewById(R.id.detailDesc);
        detailTitle = findViewById(R.id.detailTitle);
        detailImage = findViewById(R.id.detailImage);
        detailDate = findViewById(R.id.detailDate);
        deleteButton = findViewById(R.id.deleteButton);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            detailDate.setText(bundle.getString("Date"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Announcement News");
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), admin_NewsSection.class));
                    }
                });
            }
        });
    }
}