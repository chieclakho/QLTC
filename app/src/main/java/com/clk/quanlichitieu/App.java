package com.clk.quanlichitieu;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


public class App extends Application {
    public static final String CHANNEL_ID = "CHANNEL_ID";
    private static App instance;
    private Storage storage;


    @Override
    public void onCreate() {
        super.onCreate();
        storage = new Storage();
        instance = this;
        createChannelNotification();
    }

    private void createChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "QuanLiThuChi", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }
        }
    }

    public Storage getStorage() {
        return storage;
    }

    public static App getInstance() {
        return instance;
    }

}
