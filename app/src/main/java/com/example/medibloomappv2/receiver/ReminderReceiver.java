package com.example.medibloomappv2.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;

import com.example.medibloomappv2.R;
import com.example.medibloomappv2.ui.main.MainActivity;
import com.example.medibloomappv2.utils.NotificationHelper;

public class ReminderReceiver extends BroadcastReceiver {

    public static final String ACTION_MARK_TAKEN = "com.example.medibloomappv2.ACTION_MARK_TAKEN";

    @Override
    public void onReceive(Context context, Intent intent) {
        long logId = intent.getLongExtra(NotificationHelper.EXTRA_LOG_ID, -1);
        String medicineName = intent.getStringExtra(NotificationHelper.EXTRA_MEDICINE_NAME);
        String dosage = intent.getStringExtra(NotificationHelper.EXTRA_DOSAGE);

        if (medicineName == null) return;

        // Intent to open app
        Intent openIntent = new Intent(context, MainActivity.class);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent openPending = PendingIntent.getActivity(
                context, (int) logId, openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Intent for "Mark as Taken" action
        Intent takenIntent = new Intent(context, MarkTakenReceiver.class);
        takenIntent.setAction(ACTION_MARK_TAKEN);
        takenIntent.putExtra(NotificationHelper.EXTRA_LOG_ID, logId);
        takenIntent.putExtra("notification_id", (int) logId);
        PendingIntent takenPending = PendingIntent.getBroadcast(
                context, (int) (logId + 10000), takenIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Full-colour logo for the notification body (large icon slot)
        Bitmap logoBitmap = BitmapFactory.decodeResource(
                context.getResources(), R.drawable.medibloom_logo);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)   // monochrome bloom — status bar
                .setLargeIcon(logoBitmap)                   // full-colour logo — notification body
                .setContentTitle("💊 Time for " + medicineName)
                .setContentText("Take your " + dosage + " now")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(openPending)
                .addAction(R.drawable.ic_check, "Mark Taken", takenPending)
                .setColor(0xFFF48FB1);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify((int) logId, builder.build());
        }
    }
}

