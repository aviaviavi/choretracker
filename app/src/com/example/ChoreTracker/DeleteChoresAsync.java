package com.example.ChoreTracker;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

public class DeleteChoresAsync extends AsyncTask<String, Void, JSONArray> {

    private GetChoresActivity activity;
    private ProgressDialog spinner;

    public DeleteChoresAsync(GetChoresActivity act) {
        activity = act;
    }

    @Override
    protected JSONArray doInBackground(String... strings) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner = new ProgressDialog(activity);
                spinner.show();
            }
        });
        String choreName = strings[0];
        ApiCallBuilder api = ((ChoreTrackerApp) activity.getApplication()).defaultApi("delete_chore");
        api.putParam("chore_name", choreName);
        api.putParam("day", Integer.toString((new Date().getDay() - 1) % 7));
        try {
            return api.sendCall().getJSONArray("chores");
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
