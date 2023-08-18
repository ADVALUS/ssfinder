package com.example.myapplication3;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Schedule the AppMonitorWorker to run every 6 hours
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(AppMonitorWorker.class, 2, TimeUnit.MINUTES)
                .build();

        WorkManager.getInstance(this).enqueue(workRequest);
        AppViewModel appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        TextView resultTextView = findViewById(R.id.resultTextView);

        appViewModel.getResultTextLiveData().observe(this, resultText -> {
            resultTextView.setText(resultText);
        });
    }
}

