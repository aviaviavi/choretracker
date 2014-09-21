package com.example.ChoreTracker;

import android.net.Uri;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;

public class ApiCallBuilder {

    private Hashtable<String, String> params;
    private String address = "http://192.168.1.253:8080"; // "http://192.168.1.253";
    private String function;

    public ApiCallBuilder (String func, Hashtable<String, String> paramIn) {
        params = paramIn;
        function = func;
    }

    public ApiCallBuilder (String func) {
        params = new Hashtable<String, String>();
        function = func;
    }

    public void putParam(String key, String val) {
        if (val != null) {
            params.put(key, val);
        }
    }

    public String getUrl() {
        Uri.Builder builder = Uri.parse(address).buildUpon();
        builder.path(function);
        for (String key : getParams().keySet()) {
            builder.appendQueryParameter(key, getParams().get(key));
        }
        return builder.toString();
    }

    public JSONObject sendCall() throws URISyntaxException, IOException, JSONException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        URI website = new URI(getUrl());
        request.setURI(website);
        HttpResponse response = httpclient.execute(request);
        BufferedReader in;
        in = new BufferedReader(new InputStreamReader(
                response.getEntity().getContent()));
        String jsonString = in.readLine();
        JSONObject json = new JSONObject(jsonString);
        return json;
    }

    public Hashtable<String, String> getParams() {
        return params;
    }
}
