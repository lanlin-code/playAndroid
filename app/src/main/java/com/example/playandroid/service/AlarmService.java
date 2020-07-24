package com.example.playandroid.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;

import com.example.playandroid.R;
import com.example.playandroid.entity.Text;
import com.example.playandroid.executor.MyThreadPool;
import com.example.playandroid.manager.DataTransferManager;
import com.example.playandroid.model.TextModel;
import com.example.playandroid.view.WebActivity;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class AlarmService extends Service {
    public AlarmService() {
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Text text = (Text) msg.obj;
            if (text != null) sendNotification(text);
            return false;
        }
    });

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getRecommend();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int aDay = 24*60*60*1000;
        long trigger = System.currentTimeMillis() + aDay;
        Intent i = new Intent(this, AlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
        manager.set(AlarmManager.RTC, trigger, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendNotification(Text text) {

        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(DataTransferManager.KEY, text.getLink());
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel",
                    "alarm", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            notification = new NotificationCompat.Builder(this, "channel")
                    .setContentTitle(text.getTitle()).setWhen(System.currentTimeMillis()).
                            setContentIntent(pendingIntent).setSmallIcon(R.drawable.ic_launcher_foreground).build();
        } else notification = new NotificationCompat.Builder(this).
                setContentTitle(text.getTitle()).setSmallIcon(R.drawable.ic_launcher_foreground)
                .setWhen(System.currentTimeMillis()).setContentIntent(pendingIntent).build();
        notificationManager.notify(1, notification);

    }

    private void getRecommend() {
        MyThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Text recommend = TextModel.getRecommendText();
                Message message = Message.obtain();
                message.obj = recommend;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
