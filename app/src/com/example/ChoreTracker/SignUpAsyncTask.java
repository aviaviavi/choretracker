package com.example.ChoreTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

public class SignUpAsyncTask extends AsyncTask<ApiCallBuilder, Void, Void> {

    private Activity activity;

    public SignUpAsyncTask(Activity act) {
        activity = act;
    }

    @Override
    protected Void doInBackground(ApiCallBuilder... apiCallBuilders) {
        try {
            JSONObject response = apiCallBuilders[0].sendCall();
            boolean success = response.getBoolean("success");
            if (!success) {
                ;
            } else {
                // go to join group view
                Intent joinGroupIntent = new Intent(activity, JoinGroupActivity.class);
                String userName = apiCallBuilders[0].getParams().get("user_name");
                ((ChoreTrackerApp) activity.getApplication()).setUserName(userName);
                activity.startActivity(joinGroupIntent);
                activity.finish();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            runOnUi();
        }
        return null;
    }

    private void runOnUi() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText input = (EditText) activity.findViewById(R.id.userNameInput);
                input.setError("User name taken (or possible server error).");
            }
        });
    }

}
