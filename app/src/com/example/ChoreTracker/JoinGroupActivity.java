package com.example.ChoreTracker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

public class JoinGroupActivity extends Activity {

    private ListAdapter listAdapter;
    private boolean joined = false;
    private int selectedRow;
    private ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_group_activity);
        getActionBar().setTitle("Join a group");
        new GetJoinableGroupsAsync(this).execute();
        lv = (ListView) findViewById(R.id.groupListView);
    }

    public void setListAdapter(ListAdapter listAdapter) {
        this.listAdapter = listAdapter;
    }

    public ListAdapter getListAdapter() {
        return listAdapter;
    }



    public boolean isJoined() {
        return joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }
}
