package com.example.myapplication3;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppMonitorWorker extends Worker {

    public AppMonitorWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        List<PackageInfo> recentApps = getRecentInstalledApps(getApplicationContext());

        StringBuilder resultBuilder = new StringBuilder();

        for (PackageInfo packageInfo : recentApps) {
            try {
                PackageManager packageManager = getApplicationContext().getPackageManager();
                String appName = packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageInfo.packageName, PackageManager.GET_META_DATA)).toString();
                String installTime = formatDate(packageInfo.firstInstallTime);

                resultBuilder.append("App Name: ").append(appName).append("\n");
                resultBuilder.append("Install Time: ").append(installTime).append("\n\n");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        String resultText = resultBuilder.toString();
        AppViewModel appViewModel = new ViewModelProvider.AndroidViewModelFactory((Application) getApplicationContext())
                .create(AppViewModel.class);
        appViewModel.setResultText(resultText);
        return Result.success(); // Indicate success
    }

    private List<PackageInfo> getRecentInstalledApps(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        List<PackageInfo> recentApps = new ArrayList<>();

        long currentTimeMillis = System.currentTimeMillis();
        long twentyFourHoursInMillis = 24 * 60 * 60 * 1000;

        for (PackageInfo packageInfo : packageInfoList) {
            long installTime = packageInfo.firstInstallTime;
            if (currentTimeMillis - installTime < twentyFourHoursInMillis) {
                recentApps.add(packageInfo);
            }
        }

        return recentApps;
    }

    private String formatDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date(millis);
        return sdf.format(date);
    }
}
