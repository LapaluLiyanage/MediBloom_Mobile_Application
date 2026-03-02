package com.example.medibloomappv2.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.medibloomappv2.data.local.AppDatabase;
import com.example.medibloomappv2.utils.NotificationHelper;

import java.util.concurrent.Executors;

public class MarkTakenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long logId = intent.getLongExtra(NotificationHelper.EXTRA_LOG_ID, -1);
        int notifId = intent.getIntExtra("notification_id", -1);

        if (logId == -1) return;

        // Mark as taken in database
        Executors.newSingleThreadExecutor().execute(() ->
                AppDatabase.getInstance(context).doseLogDao()
                        .markAsTaken(logId, System.currentTimeMillis())
        );

        // Cancel the notification
        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null && notifId != -1) {
            manager.cancel(notifId);
        }
    }
}

