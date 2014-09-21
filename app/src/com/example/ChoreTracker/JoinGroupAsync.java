package com.example.ChoreTracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

public class JoinGroupAsync extends AsyncTask<ApiCallBuilder, Void, Void> {

    private JoinGroupActivity activity;
    private ProgressDialog spinner;

    public JoinGroupAsync(JoinGroupActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(ApiCallBuilder... apiCallBuilders) {
        ApiCallBuilder api = apiCallBuilders[0];
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner = new ProgressDialog(activity);
                spinner.show();
            }
        });
        try {
            JSONObject result = api.sendCall();
            boolean success = result.getBoolean("success");
            if (success) {
                ((ChoreTrackerApp) activity.getApplication()).setGroupName(api.getParams().get("group_name"));
                Intent getChoresIntent = new Intent(activity, GetChoresActivity.class);
                getChoresIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Activity parent = activity.getParent();
                if (parent != null) {
                    activity.getParent();
                }
                activity.startActivity(getChoresIntent);
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
