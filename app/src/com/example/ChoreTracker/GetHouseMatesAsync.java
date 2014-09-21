package com.example.ChoreTracker;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

public class GetHouseMatesAsync extends AsyncTask<Activity, Void, String[]> {

    private GetChoresActivity activity;

    @Override
    protected String[] doInBackground(Activity... args) {
        activity = (GetChoresActivity) args[0];
        ApiCallBuilder api = ((ChoreTrackerApp) activity.getApplication()).defaultApi("house_mates");
        try {
            JSONObject results = api.sendCall();
            return toStringArray(results.getJSONArray("members"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("error getting group members");
    }

    private String[] toStringArray(JSONArray array) throws JSONException{
        String[] result = new String[array.length()];
        for (int i = 0; i < result.length; i++) {
            result[i] = array.getJSONArray(i).getString(0);
        }
        return result;
    }

    @Override
    public void onPostExecute(String[] members) {
        Spinner spinner = (Spinner) activity.promptsView.findViewById(R.id.assignToSpinner);
        spinner.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, members));
    }

}
