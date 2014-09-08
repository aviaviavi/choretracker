package com.example.ChoreTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: avi
 * Date: 9/7/14
 * Time: 9:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class SignUpAsyncTask extends AsyncTask<ApiCallBuilder, Void, Void> {

    private Activity activity;

    public SignUpAsyncTask(Activity act) {
        activity = act;
    }

    @Override
    protected Void doInBackground(ApiCallBuilder... apiCallBuilders) {
        try {
            EditText input = (EditText) activity.findViewById(R.id.userNameInput);
            JSONObject response = apiCallBuilders[0].sendCall();
            boolean success = response.getBoolean("success");
            if (!success) {
                input.setError("User name taken!");
            } else {
                // go to join group view
                Intent joinGroupIntent = new Intent(activity, JoinGroupActivity.class);
                joinGroupIntent.putExtra("user_name", activity.getIntent().getStringExtra("user_name"));
                joinGroupIntent.putExtra("group_name", "");
                joinGroupIntent.putExtra("phone_number", activity.getIntent().getStringExtra("phone_number"));
                activity.startActivity(joinGroupIntent);
                activity.finish();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
}
