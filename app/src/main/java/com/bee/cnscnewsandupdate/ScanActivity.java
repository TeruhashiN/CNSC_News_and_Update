package com.bee.cnscnewsandupdate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.websitebeaver.documentscanner.DocumentScanner;

public class ScanActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        context = getApplicationContext();

        final ImageView imageView = findViewById(R.id.imageView);
        final DocumentScanner documentScanner = new DocumentScanner(
                this,
                (croppedImageResults) -> {
                    final Intent i = new Intent();
                    i.putExtra("image", croppedImageResults.get(0));
                    setResult(RESULT_OK, i);
                    finish();
                    return null;
                },
                (errorMessage) -> {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    finish();
                    return null;
                },
                () -> {
                    finish();
                    return null;
                },
                null,
                null,
                null
        );
        documentScanner.startScan();
    }
}