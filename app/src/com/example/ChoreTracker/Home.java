package com.example.ChoreTracker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import java.util.Calendar;
import java.util.Random;

public class Home extends Activity {

    public static String KEY_PHONE_NUMBER = "phoneNumber";
    public double appVersion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        appVersion = ((ChoreTrackerApp) getApplication()).VERSION;
        Random gen = new Random();
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tMgr.getLine1Number();
        // for debugging
        if (phoneNumber == null) phoneNumber = "1319999191"; //Integer.toString(gen.nextInt(1000000000) + 1000000000);
        ((ChoreTrackerApp)this.getApplication()).setPhoneNumber(phoneNumber);
        setContentView(R.layout.main);
//        Intent myIntent = new Intent((ChoreTrackerApp) getApplication(), DailyChoreNotificationService.class);
//        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getService((ChoreTrackerApp) getApplication(), 0, myIntent, 0);
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 19);
//        calendar.set(Calendar.MINUTE, 10);
//        calendar.set(Calendar.SECOND, 00);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , pendingIntent);  //set repeating every 24 hours

        new LoginAsync(this).execute(phoneNumber);
    }


}
