package com.example.ChoreTracker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import java.util.Random;

public class Home extends Activity {

    public static String KEY_PHONE_NUMBER = "phoneNumber";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Random gen = new Random();
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tMgr.getLine1Number();
        // for debugging
        if (phoneNumber == null) phoneNumber = "1111111111"; //Integer.toString(gen.nextInt(1000000000) + 1000000000);
        ((ChoreTrackerApp)this.getApplication()).setPhoneNumber(phoneNumber);
        setContentView(R.layout.main);
        new LoginAsync(this).execute(phoneNumber);
    }


}
