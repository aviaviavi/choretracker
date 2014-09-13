package com.example.ChoreTracker;

import android.content.Intent;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: avi
 * Date: 9/9/14
 * Time: 6:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoinGroupAsync extends AsyncTask<ApiCallBuilder, Void, Void> {

    private JoinGroupActivity activity;

    public JoinGroupAsync(JoinGroupActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(ApiCallBuilder... apiCallBuilders) {
        ApiCallBuilder api = apiCallBuilders[0];
        try {
            JSONObject result = api.sendCall();
            boolean success = result.getBoolean("success");
            if (success) {
                ((ChoreTrackerApp) activity.getApplication()).setGroupName(api.getParams().get("group_name"));
                Intent getChoresIntent = new Intent(activity, GetChoresActivity.class);
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
