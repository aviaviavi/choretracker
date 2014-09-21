package com.example.ChoreTracker;

import android.app.Application;

import java.util.HashMap;
import java.util.Hashtable;

public class ChoreTrackerApp extends Application {

    private String userName;
    private String phoneNumber;
    private String groupName;
    public double VERSION = 1.2;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ApiCallBuilder defaultApi(String func) {
        ApiCallBuilder api = new ApiCallBuilder(func, new Hashtable());
        api.putParam("user_name", getUserName());
        api.putParam("phone_number", getPhoneNumber());
        api.putParam("group_name", getGroupName());
        return api;
    }
}
