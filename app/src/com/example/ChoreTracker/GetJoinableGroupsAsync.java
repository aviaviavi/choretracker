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

public class GetJoinableGroupsAsync extends AsyncTask<Void, Void, Void> {

    private JoinGroupActivity activity;
    private List<String> groupList;
    private int selectedRow;
    private ApiCallBuilder joinApiCall;

    public GetJoinableGroupsAsync(JoinGroupActivity joinGroupActivity) {
        activity = joinGroupActivity;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        ApiCallBuilder api = new ApiCallBuilder("groups", new Hashtable<String, String>());
        groupList = new ArrayList<String>();
        ListView lv = (ListView) activity.findViewById(R.id.groupListView);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Join this group?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                GetJoinableGroupsAsync.this.setSelectedRow(i);
            }
        });
        try {
            JSONArray groups = api.sendCall().getJSONArray("groups");
            for (int i = 0; i < groups.length(); i++) {
                groupList.add(groups.getString(i));
            }
            populateGroupApapter();
            return null;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
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
                    ((ChoreTrackerApp) activity.getApplication()).setGroupName(groupName);
                    ApiCallBuilder api = ((ChoreTrackerApp) activity.getApplication()).defaultApi("join_group");
                    joinGroup(api);
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

    private void joinGroup(ApiCallBuilder api) {
        joinApiCall = api;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new JoinGroupAsync(activity).execute(joinApiCall);
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
