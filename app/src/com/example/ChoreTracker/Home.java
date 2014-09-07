package com.example.ChoreTracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class Home extends Activity {

    public static String KEY_PHONE_NUMBER = "phoneNumber";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        if (!userExists(mPhoneNumber)) {
            // route to sign up activity
            Intent signUpIntent = new Intent(this, SignUp.class);
            signUpIntent.putExtra(KEY_PHONE_NUMBER, mPhoneNumber);
            startActivity(signUpIntent);
        }
        setContentView(R.layout.main);
    }

    public Boolean userExists(String phoneNumber) {
        try {
            String url = "http://localhost:8080/?phone_number=" + phoneNumber;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI website = new URI(url);
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            String jsonString = in.readLine();
            JSONObject json = new JSONObject(jsonString);
            return json.getBoolean("user_exists");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("error connecting to server");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("error connecting to server");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("error connecting to server");
        }
    }
}
