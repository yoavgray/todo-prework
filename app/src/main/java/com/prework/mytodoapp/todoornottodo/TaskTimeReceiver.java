package com.prework.mytodoapp.todoornottodo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TaskTimeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        serviceIntent.putExtras(intent.getExtras());
        context.startService(serviceIntent);
    }
}
