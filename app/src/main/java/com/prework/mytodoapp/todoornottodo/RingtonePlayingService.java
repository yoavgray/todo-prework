package com.prework.mytodoapp.todoornottodo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

public class RingtonePlayingService extends Service {
    private NotificationManager mNM;
    MediaPlayer media;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int callingId;
    private String task, date, time;
    private int taskId;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Extracting values from coming Intent
        task = intent.getStringExtra("task");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        taskId = intent.getIntExtra("taskId",0);

        //Play music
        media = MediaPlayer.create(this, R.raw.errie);
        media.start();

        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification.
        callingId = startId;
        showNotification(taskId);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        //Cancel notification.
        mNM.cancel(callingId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotification(int startId) {
        //Text to be shown in the notification
        CharSequence text = "Reminder: " + task;

        //The PendingIntent to launch our activity if the user clicks on notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        //Intent to launch our desired activity and mark the notification as completed
        Intent mainActivityIntent = new Intent(this,MainActivity.class);
        //To differentiate between to notifications
        mainActivityIntent.setAction("" + startId);
        mainActivityIntent.putExtra("done",true);
        mainActivityIntent.putExtra("id",startId);
        PendingIntent pendingIntentCompleted = PendingIntent.getActivity(this, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the info for the views that show in the notification panel.
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(date + " " + time)  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .addAction(R.drawable.ic_done_black_24dp, "Done", pendingIntentCompleted)
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

        // Send the notification.
        mNM.notify(startId, notification);
    }
}
