package com.example.ChoreTracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;

public class GetChoresActivity extends Activity {

    private String userName;
    private String groupName;
    private String phoneNumber;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chores_view);
        getActionBar().setTitle("Today's Chores");
        phoneNumber = ((ChoreTrackerApp) getApplication()).getPhoneNumber();
        groupName = ((ChoreTrackerApp) getApplication()).getGroupName();
        userName = ((ChoreTrackerApp) getApplication()).getUserName();
        String choresString = getIntent().getStringExtra("json");
        if (choresString == null) {
            new LoginAsync(this).execute(phoneNumber);
        } else {
            try {
                JSONArray chores = new JSONArray(choresString);
                if (chores.length() != 0) {
                    paintChores(chores);
                }
            } catch (JSONException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private void paintChores(JSONArray chores) throws JSONException {
        String[] choreArray = new String[chores.length()];
        for (int i = 0; i < choreArray.length; i++) {
            choreArray[i] = chores.getJSONArray(i).getString(0);
        }
        ListView lv = (ListView) findViewById(R.id.choresListView);
        TextView tv = (TextView) findViewById(R.id.noChoresText);
        tv.setVisibility(View.INVISIBLE);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1));
    }

}