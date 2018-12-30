package ru.pschsch.pschschapps.studyingapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
    public MyService() {
    }
    @Override
    public void onCreate(){
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent, flags, startId);
    }
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null; //если здесь вернуть null, то он будет считаться started service
    }
}
