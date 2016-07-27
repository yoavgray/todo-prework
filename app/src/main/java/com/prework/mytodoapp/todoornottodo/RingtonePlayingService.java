package com.prework.mytodoapp.todoornottodo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class RingtonePlayingService extends Service {
    private NotificationManager mNM;
    MediaPlayer media;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int callingId;
    private String task, date, time;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        task = intent.getStringExtra("task");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");

        media = MediaPlayer.create(this, R.raw.errie);
        media.start();

        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
        callingId = startId;
        showNotification(callingId);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(callingId);

        // Tell the user we stopped.
        Toast.makeText(this, "Service ended", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Show a notification while this service is running.
     * @param startId
     */
    private void showNotification(int startId) {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = "Reminder: " + task;

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(date + " " + time)  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

        // Send the notification.
        mNM.notify(startId, notification);
    }
}
