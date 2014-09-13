package com.example.ChoreTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.TextView;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.List;

public class LoginAsync extends AsyncTask<String, Void, JSONObject> {

    private Activity activity;
    private String phoneNumber;

    public LoginAsync(Activity act) {
       activity = act;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        phoneNumber = strings[0];
        return getUserForNumber(phoneNumber);
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        try {
            super.onPostExecute(json);
            String userName = json.getString("user_name");
            if (userName == "null") {
                Intent signUp = new Intent(activity, SignUp.class);
                signUp.putExtra(Home.KEY_PHONE_NUMBER, phoneNumber);
                activity.startActivity(signUp);
                activity.finish();
            } else {
                ((ChoreTrackerApp) activity.getApplication()).setUserName(userName);
                String groupName = json.getString("group_name");
                if (groupName.equals("null")) {
                    Intent joinGroupIntent = new Intent(activity, JoinGroupActivity.class);
                    joinGroupIntent.putExtra("user_name", activity.getIntent().getStringExtra("user_name"));
                    joinGroupIntent.putExtra("group_name", "");
                    joinGroupIntent.putExtra("phone_number", activity.getIntent().getStringExtra("phone_number"));
                    activity.startActivity(joinGroupIntent);
                    activity.finish();
                } else {
                    JSONArray chores = json.getJSONArray("chores");
                    Intent choresIntent = new Intent(activity, GetChoresActivity.class);
                    choresIntent.putExtra("json", chores.toString());
                    activity.startActivity(choresIntent);
                    activity.finish();
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException("caught error parsing JSON");
        }
    }

    public JSONObject getUserForNumber(String phoneNumber) {
        Hashtable<String, String> params = new Hashtable<String, String>();
        params.put("phone_number", phoneNumber);
        try {
            return new ApiCallBuilder("", params).sendCall();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("error getting json");
    }
}
