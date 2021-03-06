package com.example.ChoreTracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class GetGroupsAsyncTask extends AsyncTask<Void, Void, Void> {

    private JoinGroupActivity activity;
    private List<String> groupList;
    private int selectedRow;

    public GetGroupsAsyncTask(JoinGroupActivity joinGroupActivity) {
        activity = joinGroupActivity;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        ApiCallBuilder api = new ApiCallBuilder("groups", new Hashtable<String, String>());
        groupList = new ArrayList<String>();
        try {
            JSONArray groups = api.sendCall().getJSONArray("groups");
            for (int i = 0; i < groups.length(); i++) {
                groupList.add(groups.getString(i));
            }
            populateGroupApapter();
            return null;
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new RuntimeException("caught error getting group names");
    }

     public DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    ListView lv = (ListView) activity.findViewById(R.id.groupListView);
                    String groupName = (String) lv.getAdapter().getItem(getSelectedRow());
                    String userName = ((ChoreTrackerApp) activity.getApplication()).getUserName();
                    String phoneNumber = ((ChoreTrackerApp) activity.getApplication()).getPhoneNumber();
                    Hashtable<String,String> params = new Hashtable<String, String>();
                    // TODO: put this inside every apicallbuilder
                    params.put("group_name", groupName);
                    params.put("user_name", userName);
                    params.put("phone_number", phoneNumber);
                    ApiCallBuilder api = new ApiCallBuilder("join_group", params);


                    try {
                        JSONObject results = api.sendCall();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (JSONException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    private void populateGroupApapter() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final ArrayAdapter<String> groupListAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, Arrays.copyOf(groupList.toArray(), groupList.size(), String[].class));
                ListView lv = (ListView) activity.findViewById(R.id.groupListView);
                lv.setAdapter(groupListAdapter);
            }
        });
    }

    public void setSelectedRow(int getSelectedRow) {
        this.selectedRow = getSelectedRow;
    }

    public int getSelectedRow() {
        return selectedRow;
    }
}
