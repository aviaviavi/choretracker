package com.example.ChoreTracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;

public class GetChoresActivity extends Activity {

    private String userName;
    private String groupName;
    private String phoneNumber;
    private ListView lv;
    private TextView tv;
    private final String[] DAYS_OF_WEEK = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    public View promptsView;
    private int selectedRow;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chores_view);
        phoneNumber = ((ChoreTrackerApp) getApplication()).getPhoneNumber();
        groupName = ((ChoreTrackerApp) getApplication()).getGroupName();
        userName = ((ChoreTrackerApp) getApplication()).getUserName();
        double newestVersion = getIntent().getDoubleExtra("newest_version", 0.1);
        if (((ChoreTrackerApp) getApplication()).VERSION < newestVersion) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Upgrade");
            builder.setMessage("Update available, ready to upgrade?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(new ApiCallBuilder("static/ChoreTracker.apk").getUrl()));
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        getActionBar().setTitle("Today's Chores");
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
                e.printStackTrace();
            }
        }
    }

    private void paintChores(JSONArray chores) throws JSONException {
        String[] choreArray = new String[chores.length()];
        for (int i = 0; i < choreArray.length; i++) {
            choreArray[i] = chores.getJSONArray(i).getString(0);
        }
        lv = (ListView) findViewById(R.id.choresListView);
        tv = (TextView) findViewById(R.id.noChoresText);
        tv.setVisibility(View.INVISIBLE);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, choreArray));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder =  new AlertDialog.Builder(GetChoresActivity.this);
                builder.setMessage("Delete this chore?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                GetChoresActivity.this.setSelectedRow(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.get_chores_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.joinGroup:
                Intent intent = new Intent(this, JoinGroupActivity.class);
                intent.putExtra("activity", "getChores");
                startActivity(intent);
                return true;
            case R.id.addChore:
                LayoutInflater li = getLayoutInflater();
                promptsView = li.inflate(R.layout.create_chore, null);

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setView(promptsView);
                dialogBuilder.setTitle("New Chore");
                final EditText userInput = (EditText) promptsView.findViewById(R.id.inputChoreName);
                // set dialogBuilder message
                AlertDialog.Builder builder = dialogBuilder.setCancelable(true)
                        .setPositiveButton("Add Chore", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String choreName = ((EditText) GetChoresActivity.this.promptsView.findViewById(R.id.inputChoreName)).getText().toString();
                                if (!choreName.isEmpty()) {
                                    String assignee = ((Spinner) GetChoresActivity.this.promptsView.findViewById(R.id.assignToSpinner)).getSelectedItem().toString();
                                    String day = ((Spinner) GetChoresActivity.this.promptsView.findViewById(R.id.dayOfWeekSpinner)).getSelectedItem().toString();
                                    boolean repeat = ((CheckBox) GetChoresActivity.this.promptsView.findViewById(R.id.repeatCheckBox)).isChecked();
                                    int dayOfWeek = Arrays.asList(DAYS_OF_WEEK).indexOf(day);
                                    ApiCallBuilder api = ((ChoreTrackerApp) getApplication()).defaultApi("create_chore");
                                    api.putParam("chore_name", choreName);
                                    api.putParam("user_name", assignee);
                                    api.putParam("day", Integer.toString(dayOfWeek));
                                    api.putParam("repeat", repeat ? "1" : "0");
                                    new CreateChoreAsync(GetChoresActivity.this).execute(api);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                Spinner spinner = (Spinner) promptsView.findViewById(R.id.dayOfWeekSpinner);
                spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DAYS_OF_WEEK));
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                new GetHouseMatesAsync().execute(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void resetChoreActivity(JSONArray choresArray) {
        Intent intent = new Intent(this, GetChoresActivity.class);
        intent.putExtra("json", choresArray.toString());
        startActivity(intent);
        finish();
    }

    public DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    String choreName = lv.getAdapter().getItem(getSelectedRow()).toString();
                    new DeleteChoresAsync(GetChoresActivity.this).execute(choreName);
                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    public int getSelectedRow() {
        return selectedRow;
    }

    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;
        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {

            // Do whatever you want here

            // If tou want to close the dialog, uncomment the line below
            //dialog.dismiss();
        }
    }
}