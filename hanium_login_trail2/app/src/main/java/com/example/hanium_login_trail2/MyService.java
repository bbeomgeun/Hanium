package com.example.hanium_login_trail2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.NonNull;

public class MyService extends Service {
    NotificationManager notificationManager;
    Notification notification;
    ServiceThread thread;

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        thread.stopForever();
        thread = null;
    }

    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //TODO : handler알아보기 ( 메세지에 내용 담을 수 있는지도)
            // + 시스템을 앱 킬때 실행시킬지? 그것에 대해 생각해보기 ( 앱 깔린 이후에 백그라운드에서 계속 돌아가면서 체크를 해야한다)
            Intent intent = new Intent(MyService.this, fragment_main.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this,0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
            notification = new Notification.Builder(getApplicationContext())
                    .setContentText("testText")
                    .setContentTitle("testTitle")
                    .setContentIntent(pendingIntent)
                    .build();

            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(777, notification);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
