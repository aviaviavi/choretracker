package com.example.ChoreTracker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SignUp extends Activity {

    private String phoneNumber;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phoneNumber = getIntent().getStringExtra(Home.KEY_PHONE_NUMBER);
        setContentView(R.layout.main);
        TextView tv = (TextView) findViewById(R.id.mainTextView);
        tv.setText(phoneNumber);
    }
}