package com.example.ChoreTracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Hashtable;

public class SignUp extends Activity {

    private String phoneNumber;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phoneNumber = getIntent().getStringExtra(Home.KEY_PHONE_NUMBER);
        setContentView(R.layout.signup);
        Button submit = (Button) findViewById(R.id.signUpButton);
        submit.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.userNameInput);
                Hashtable<String, String> params = new Hashtable<String, String>();
                String userName = input.getText().toString();
                params.put("user_name", userName);
                params.put("phone_number", phoneNumber);
                if (input != null && input.length() >= 3) {
                    new SignUpAsyncTask(SignUp.this).execute(new ApiCallBuilder("create_user", params));
                } else {
                    input.setError("User name too short!");
                }
            }
        });
    }
}