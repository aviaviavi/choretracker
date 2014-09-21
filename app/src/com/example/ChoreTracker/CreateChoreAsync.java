package com.example.ChoreTracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

public class CreateChoreAsync extends AsyncTask<ApiCallBuilder, Void, JSONArray> {

    private GetChoresActivity activity;
    private ProgressDialog spinner;

    public CreateChoreAsync(GetChoresActivity act) {
        activity = act;
    }

    @Override
    protected JSONArray doInBackground(ApiCallBuilder... apiCallBuilders) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner = new ProgressDialog(activity);
                spinner.show();
            }
        });
        ApiCallBuilder api = apiCallBuilders[0];
        try {
            JSONObject results = api.sendCall();
            return results.getJSONArray("chores");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    @Override
    protected void onPostExecute(JSONArray choresArray) {
        spinner.dismiss();
        activity.resetChoreActivity(choresArray);
    }
}
