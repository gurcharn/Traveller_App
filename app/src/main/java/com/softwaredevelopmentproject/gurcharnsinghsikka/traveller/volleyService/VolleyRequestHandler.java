package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.volleyService;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gurcharn Singh Sikka
 * @version 1.0
 *
 * Class to handle all volley requests to API
 */
public class VolleyRequestHandler {

    private final String domain;

    private Context context;
    private RequestQueue requestQueue;

    /**
     * Constructor
     *
     * @param context
     */
    public VolleyRequestHandler(Context context) {
        this.domain = context.getResources().getString(R.string.domain);
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    /**
     * Method to check if device have any coonected network
     * @return boolean
     */
    private boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = connectivityManager.getAllNetworkInfo();

        if(netInfo == null || netInfo.length == 0)
            return false;
        else{
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
            return haveConnectedWifi || haveConnectedMobile;
        }
    }

    /**
     * Method to check if device have active internet connectorion or not
     * @return boolean
     */
    public boolean hasActiveInternetConnection() {
        if (isNetworkAvailable()) {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://clients3.google.com/generate_204").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);
            } catch (IOException e) {
                e.getMessage();
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * Method to handle GET requests to API
     * @param endpoint
     * @param listenerResponse
     * @param listenerError
     */
    public void getRequest(String endpoint, Response.Listener<JSONObject> listenerResponse, Response.ErrorListener listenerError){
        final String url = domain + endpoint;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, listenerResponse, listenerError);
        requestQueue.add(request);
    }

    /**
     * Method to handle POST requests to API
     * @param endpoint
     * @param jsonBody
     * @param listenerResponse
     * @param listenerError
     */
    public void postRequest(String endpoint, JSONObject jsonBody, Response.Listener<JSONObject> listenerResponse, Response.ErrorListener listenerError, final String token){
        final String url = domain + endpoint;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody, listenerResponse, listenerError){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Content-Type", "application/json");
                headers.put("Authorisation", "Token " + token);
                return headers;
            }
        };
        requestQueue.add(request);
    }
}
