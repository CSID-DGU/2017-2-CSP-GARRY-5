package org.androidtown.smart_diaper;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class Bluno_BroadcastD extends BroadcastReceiver {
    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;
    Activity mActivity;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        //알람 시간이 되었을때 onReceive를 호출함
        //NotificationManager 안드로이드 상태바에 메세지를 던지기위한 서비스 불러오고
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, Bluno_MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.noti)
                .setTicker("알림")
                .setNumber(1)
                .setContentTitle("애기똥플의 알림")
                .setContentText("기저귀를 확인해 주세요.")
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent).setAutoCancel(true);

        notificationmanager.notify(1, builder.build());
        ((Bluno_MainActivity)Bluno_MainActivity.mContext).reset2();
        ((Bluno_MainActivity)Bluno_MainActivity.mContext).printData();

        //PendingIntent.FLAG_UPDATE_CURRENT

    }
}