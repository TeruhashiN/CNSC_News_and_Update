package com.bee.cnscnewsandupdate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScholarshipActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private ViewPager viewPager;
    private ScholarshipPager scholarshipPager;
    private Scholarship scholarship;
    private Context context;
    private Gson gson;
    private List<String> uris = new ArrayList<>();
    private RequirementsFragment requirementsFragment;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private ProgressDialog progressDialog;
    private boolean alreadyApplied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship);
        final FirebaseApp secondary = FirebaseApp.getInstance("educasst");
        firebaseFirestore = FirebaseFirestore.getInstance(secondary);
        firebaseStorage = FirebaseStorage.getInstance(secondary);
        firebaseAuth = FirebaseAuth.getInstance();
        context = getApplicationContext();
        gson = new Gson();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);

        final AppCompatImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            finish();
        });

        final Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Toast.makeText(context, "Problem occurred", Toast.LENGTH_SHORT).show();
            return;
        }

        final String u = bundle.getString("scholarship");
        scholarship = gson.fromJson(u, Scholarship.class);

        viewPager = findViewById(R.id.viewPager);
        requirementsFragment = new RequirementsFragment();
        scholarshipPager = new ScholarshipPager(getSupportFragmentManager(), requirementsFragment);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(scholarshipPager);

        final TextView info = findViewById(R.id.info);
        final String uid = firebaseAuth.getCurrentUser().getUid();
        alreadyApplied = scholarship.getApplicants().contains(uid);
        if (alreadyApplied) {
            info.setVisibility(View.VISIBLE);

            if (scholarship.getAccepted() != null && scholarship.getAccepted().contains(uid)) {
                if (scholarship.getReleased() != null && scholarship.getReleased().contains(uid)) {
                    info.setText("Educational assistance has been released.");
                    info.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_700));
                } else {
                    info.setText("Your application has been accepted. Click here to continue.");
                    info.setOnClickListener(v -> {
                        selectMode();
                    });
                }
            }

            if (scholarship.getDeclined() != null && scholarship.getDeclined().contains(uid)) {
                info.setText("Your application has been declined.");
                info.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
            }
        }

        uris.add("");
        uris.add("");
        uris.add("");
    }

    private void selectMode() {
        progressDialog.show();

        final String uid = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("Scholarships/" + scholarship.docid + "/Applicants").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getException() != null) {
                    progressDialog.dismiss();
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (task.isSuccessful()) {
                    final DocumentSnapshot doc = task.getResult();
                    final Applicant applicant = doc.toObject(Applicant.class).withId(doc.getId());
                    progressDialog.dismiss();
                    if (applicant.getStatus().equals("accepted")) {
                        displayDialog(applicant);
                    } else {
                        Toast.makeText(context, "Release of assistance is on process", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void displayDialog(Applicant applicant) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_selection, null);
        final RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);

        final TextInputLayout account_input = dialogView.findViewById(R.id.account_input);
        final TextInputEditText account = dialogView.findViewById(R.id.account);
        final TextInputLayout name_input = dialogView.findViewById(R.id.name_input);
        final TextInputEditText name = dialogView.findViewById(R.id.name);
        final TextInputLayout schedule_input = dialogView.findViewById(R.id.schedule_input);
        final AppCompatAutoCompleteTextView schedule = dialogView.findViewById(R.id.schedule);

        account.addTextChangedListener(new CustomTextWatcher(account_input));
        name.addTextChangedListener(new CustomTextWatcher(name_input));
        schedule.addTextChangedListener(new CustomTextWatcher(schedule_input));

        final SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        final List<String> list = new ArrayList<>();
        for (Date sched : applicant.getSchedules()) {
            String date = sdf.format(sched);
            list.add(date);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        schedule.setAdapter(adapter);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final RadioButton radioButton = (RadioButton) dialogView.findViewById(checkedId);
                final String mode = radioButton.getText().toString();
                if (mode.equals("GCash")) {
                    schedule_input.setVisibility(View.GONE);
                    account_input.setVisibility(View.VISIBLE);
                    name_input.setVisibility(View.VISIBLE);
                } else {
                    schedule_input.setVisibility(View.VISIBLE);
                    account_input.setVisibility(View.GONE);
                    name_input.setVisibility(View.GONE);
                }
            }
        });

        dialogBuilder.setTitle("Select mode of release");
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Confirm", null);
        dialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button button = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(v -> {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    if (selectedId == -1) {
                        Toast.makeText(context, "Select a mode", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    final RadioButton radioButton = (RadioButton) dialogView.findViewById(selectedId);
                    final String mode = radioButton.getText().toString();

                    if (mode.equals("GCash")) {
                        final String acc = account.getText().toString();
                        final String nam = name.getText().toString();

                        if (acc.trim().equals("")) {
                            final String err = "Account no. is empty";
                            account_input.setError(err);
                        } else  if (nam.trim().equals("")) {
                            final String err = "Account name is empty";
                            name_input.setError(err);
                        } else {
                            final Map<String, Object> data = new HashMap<>();
                            data.put("mode", mode);
                            data.put("accountNumber", acc);
                            data.put("accountName", nam);
                            dialog.dismiss();
                            update(data, applicant);
                        }
                    } else {
                        final String sched = schedule.getText().toString();
                        if (sched.trim().equals("")) {
                            final String err = "Schedule is empty";
                            schedule_input.setError(err);
                        } else if (!list.contains(sched)) {
                            final String err = "Invalid schedule";
                            schedule_input.setError(err);
                        } else {
                            final Map<String, Object> data = new HashMap<>();
                            data.put("mode", mode);
                            final int index = list.indexOf(sched);
                            data.put("schedule", applicant.getSchedules().get(index));
                            dialog.dismiss();
                            update(data, applicant);
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void update(Map<String, Object> data, Applicant applicant) {
        progressDialog.show();
        data.put("status", "requesting");

        final WriteBatch batch = firebaseFirestore.batch();
        final FieldValue now = FieldValue.serverTimestamp();

        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        final DocumentReference applicationRef = firebaseFirestore.collection("Scholarships/" + scholarship.docid + "/Applicants")
                .document(currentUser.getUid());

        final DocumentReference adminNotificationRef = firebaseFirestore.collection("Notifications").document();
        final Map<String, Object> adminNotificationData = new HashMap<>();
        adminNotificationData.put("to", "admin");
        adminNotificationData.put("from", firebaseAuth.getCurrentUser().getUid());
        adminNotificationData.put("type", "application_request");
        adminNotificationData.put("createdAt", FieldValue.serverTimestamp());
        adminNotificationData.put("scholarship", scholarship.docid);
        String adminMsg, userMsg;
        if (data.get("mode").equals("GCash")) {
            adminMsg = currentUser.getDisplayName() + " is requesting for release of assistance thru GCash.";
            userMsg = "Successfully requested for release of assistance thru GCash.";
        } else {
            adminMsg = currentUser.getDisplayName() + " has selected a schedule for release of assistance.";
            userMsg = "Successfully selected a schedule for release of assistance.";
        }
        adminNotificationData.put("message", adminMsg);

        final DocumentReference userNotificationRef = firebaseFirestore.collection("Notifications").document();
        final Map<String, Object> userNotificationData = new HashMap<>();
        userNotificationData.put("to", firebaseAuth.getCurrentUser().getUid());
        userNotificationData.put("from", "admin");
        userNotificationData.put("type", "application_request");
        userNotificationData.put("createdAt", now);
        userNotificationData.put("scholarship", scholarship.docid);
        userNotificationData.put("message", userMsg);

        batch.update(applicationRef, data);
        batch.set(adminNotificationRef, adminNotificationData);
        batch.set(userNotificationRef, userNotificationData);

        batch.commit().addOnCompleteListener(task -> {
            if (task.getException() != null) {
                progressDialog.dismiss();
                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            if (task.isSuccessful()) {
                Toast.makeText(context, "Request success", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    public boolean isAlreadyApplied() {
        return alreadyApplied;
    }

    public FirebaseFirestore getFirebaseFirestore() {
        return firebaseFirestore;
    }

    public Scholarship getScholarship() {
        return scholarship;
    }

    public void next() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    public void scan(int num) {
        final Intent i = new Intent(this, ScanActivity.class);
        startActivityForResult(i, num);
    }

    public List<String> getUris() {
        return uris;
    }

    public void apply() {
        progressDialog.show();
        final List<String> downloadUris = new ArrayList<>();

        for (String uri : uris) {
            final String[] fileArr = uri.split("/");
            final String filename = fileArr[fileArr.length - 1];
            final StorageReference storageReference = firebaseStorage.getReference().child(scholarship.docid).child(filename);

            try {
                final File file = new File(uri);
                final Uri fileUri = Uri.fromFile(file);
                final InputStream stream = getContentResolver().openInputStream(fileUri);
                final UploadTask uploadTask = storageReference.putStream(stream);

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            throw task.getException();
                        }

                        return storageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.getException() != null) {
                            progressDialog.dismiss();
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        final Uri downloadUri = task.getResult();
                        downloadUris.add(downloadUri.toString());
                        if (downloadUris.size() == 3) {
                            write(downloadUris);
                        }
                    }
                });
            } catch (FileNotFoundException e) {
                progressDialog.dismiss();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private void write(List<String> downloadUris) {
        final WriteBatch batch = firebaseFirestore.batch();
        final FieldValue now = FieldValue.serverTimestamp();

        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        final DocumentReference applicationRef = firebaseFirestore.collection("Scholarships/" + scholarship.docid + "/Applicants")
            .document(currentUser.getUid());
        final Map<String, Object> applicationData = new HashMap<>();
        applicationData.put("attachments", downloadUris);
        applicationData.put("createdAt", now);
        applicationData.put("status", "pending");

        final DocumentReference adminNotificationRef = firebaseFirestore.collection("Notifications").document();
        final Map<String, Object> adminNotificationData = new HashMap<>();
        adminNotificationData.put("to", "admin");
        adminNotificationData.put("from", firebaseAuth.getCurrentUser().getUid());
        adminNotificationData.put("type", "application");
        adminNotificationData.put("createdAt", FieldValue.serverTimestamp());
        adminNotificationData.put("scholarship", scholarship.docid);
        adminNotificationData.put("message", currentUser.getDisplayName() + " applied for " + scholarship.getTitle() + ".");

        final DocumentReference userNotificationRef = firebaseFirestore.collection("Notifications").document();
        final Map<String, Object> userNotificationData = new HashMap<>();
        userNotificationData.put("to", firebaseAuth.getCurrentUser().getUid());
        userNotificationData.put("from", "admin");
        userNotificationData.put("type", "application");
        userNotificationData.put("createdAt", now);
        userNotificationData.put("scholarship", scholarship.docid);
        userNotificationData.put("message", "You have successfully submitted your application to " + scholarship.getTitle() + ".");

        final DocumentReference scholarshipRef = firebaseFirestore.collection("Scholarships").document(scholarship.docid);
        final Map<String, Object> scholarshipData = new HashMap<>();
        scholarshipData.put("applicants", FieldValue.arrayUnion(firebaseAuth.getCurrentUser().getUid()));

        batch.set(applicationRef, applicationData);
        batch.set(adminNotificationRef, adminNotificationData);
        batch.set(userNotificationRef, userNotificationData);
        batch.update(scholarshipRef, scholarshipData);

        batch.commit().addOnCompleteListener(task -> {
            if (task.getException() != null) {
                progressDialog.dismiss();
                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            if (task.isSuccessful()) {
                Toast.makeText(context, "Application success", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Intent i = new Intent();
                i.putExtra("scholarship", scholarship.docid);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (data != null) {
                final String res = data.getStringExtra("image");
                uris.set(requestCode, res);
                requirementsFragment.updateViews(requestCode);
            }
        }
    }
}