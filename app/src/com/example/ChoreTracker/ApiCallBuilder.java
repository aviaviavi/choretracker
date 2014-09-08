package com.example.ChoreTracker;

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

/**
 * Created with IntelliJ IDEA.
 * User: avi
 * Date: 9/7/14
 * Time: 9:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApiCallBuilder {

    private Hashtable<String, String> params;
    private String address = "http://10.0.3.2:8080/";
    private String function;
    private StringBuilder url = new StringBuilder();

    public ApiCallBuilder (String func, Hashtable<String, String> params) {
        function = func;
        url.append(address);
        url.append(function);
        url.append("?");
        for (String key : params.keySet()) {
            url.append(key + "=" + params.get(key) + "&");
        }
    }

    public String getUrl() {
        return url.toString();
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


}