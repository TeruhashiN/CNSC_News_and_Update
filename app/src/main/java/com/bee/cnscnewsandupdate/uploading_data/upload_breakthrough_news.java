package com.bee.cnscnewsandupdate.uploading_data;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bee.cnscnewsandupdate.Announcement_data.DataClass;
import com.bee.cnscnewsandupdate.notification.FcmNotificationsSender;
import com.bee.cnscnewsandupdate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


public class upload_breakthrough_news extends AppCompatActivity {

    int hour1, minute1;

    ImageView uploadImage;
    Button saveButton;
    EditText uploadTitle, uploadDesc, uploadDate;
    String imageURL, NotifTitle, NotifMessage;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_breakthrough_news);


        uploadImage = findViewById(R.id.uploadImage);
        uploadTitle = findViewById(R.id.uploadTitle);
        uploadDesc = findViewById(R.id.uploadDesc);
        uploadDate = findViewById(R.id.uploadDate);
        saveButton = findViewById(R.id.saveButton);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Manila"));
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        NotifTitle = "Announcement!";
        NotifMessage = "Breakthrough News has been updated! Check them out.";


        ActivityResultLauncher<Intent>activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(upload_breakthrough_news.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        uploadDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view1, int year1, int month1, int dayOfMonth) {
                    month1 = month1 + 1;
                    String date = year1 + "-" + month1 + "-" + dayOfMonth;
                    uploadDate.setText(date);

                    // Create a TimePickerDialog to allow the user to select a time
                    TimePickerDialog timePickerDialog = new TimePickerDialog(upload_breakthrough_news.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            // Convert 24-hour format to 12-hour format
                            String AM_PM;
                            if (hourOfDay < 12) {
                                AM_PM = "AM";
                                if (hourOfDay == 0) {
                                    hour1 = 12;
                                } else {
                                    hour1 = hourOfDay;
                                }
                            } else {
                                AM_PM = "PM";
                                if (hourOfDay == 12) {
                                    hour1 = 12;
                                } else {
                                    hour1 = hourOfDay - 12;
                                }
                            }
                            // Format the time
                            String time = String.format(Locale.getDefault(), "%02d:%02d %s", hour1, minute, AM_PM);
                            uploadDate.setText(uploadDate.getText() + " " + time);
                        }
                    }, hour, minute, false); // By typing a False, it will make 24 hours into AM and PM naisu desu
                    timePickerDialog.show();
                }
            }, year, month, day);
            datePickerDialog.show();
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the EditText fields are empty
                if (uploadTitle.getText().toString().isEmpty() ||
                        uploadDesc.getText().toString().isEmpty() ||
                        uploadDate.getText().toString().isEmpty()) {

                    // Show a dialog box with an error message
                    AlertDialog.Builder builder = new AlertDialog.Builder(upload_breakthrough_news.this);
                    builder.setMessage("Please fill in all the fields");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    saveData();
                }
            }
        });


    }
    public void saveData() {
        if (uploadTitle.getText().toString().isEmpty()) {
            uploadTitle.setError("Title is required");
            return;
        }
        if (uploadDesc.getText().toString().isEmpty()) {
            uploadDesc.setError("Description is required");
            return;
        }
        if (uri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images / Breakthrough Section")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(upload_breakthrough_news.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                uploadData();
                dialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();

            }
        });
    }
    public void uploadData() {

        String title = uploadTitle.getText().toString();
        String desc = uploadDesc.getText().toString();
        String date = uploadDate.getText().toString();

        DataClass dataClass = new DataClass(title, desc, date, imageURL);

        String currentDate = uploadDate.getText().toString();

        FirebaseDatabase.getInstance().getReference("Breakthrough News").child(String.valueOf(currentDate))
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(upload_breakthrough_news.this, "Saved", Toast.LENGTH_SHORT).show();
                            // Notification
                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/All",NotifTitle, NotifMessage, getApplicationContext(), upload_breakthrough_news.this);
                            notificationsSender.SendNotifications();
                            finish();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(upload_breakthrough_news.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


}
