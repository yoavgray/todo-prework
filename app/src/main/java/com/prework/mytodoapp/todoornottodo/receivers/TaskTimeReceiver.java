package com.prework.mytodoapp.todoornottodo.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.prework.mytodoapp.todoornottodo.services.RingtonePlayingService;

public class TaskTimeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        serviceIntent.putExtras(intent.getExtras());
        context.startService(serviceIntent);
    }
}
