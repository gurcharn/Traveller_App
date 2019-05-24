package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.places;

import android.content.Context;
import android.graphics.Color;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login.Login;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login.LoginLocalDAO;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.Profile;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.ProfileLocalDAO;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips.Trip;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.volleyService.VolleyRequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlacesRemoteDAO {

    private final String endpoint;
    private final String textSearchEndpoint;
    private final String nearbyEndpoint;
    private Context context;
    private VolleyRequestHandler volleyRequestHandler;
    private PlacesFragment placesFragment;
    private LoginLocalDAO loginLocalDAO;
    private ProfileLocalDAO profileLocalDAO;

    public PlacesRemoteDAO(Context context, PlacesFragment placesFragment) {
        this.endpoint = context.getResources().getString(R.string.places_endpoint);
        this.textSearchEndpoint = context.getResources().getString(R.string.textSearch_endpoint);
        this.nearbyEndpoint = context.getResources().getString(R.string.nearby_endpoint);
        this.context = context;
        this.placesFragment = placesFragment;
        this.volleyRequestHandler = new VolleyRequestHandler(context);
        this.loginLocalDAO = new LoginLocalDAO(context);
        this.profileLocalDAO = new ProfileLocalDAO(context);
    }

    public void placesTextSearchHandler(final Trip trip){
        android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                JSONArray jsonArrayBody = new JSONArray();
                JSONObject jsonBody = new JSONObject();

                try {
                    jsonBody.put("place", trip.getPlace());
                    jsonBody.put("interests", getInterests(trip.getUserId()));
                    jsonArrayBody.put(jsonBody);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Response.Listener<JSONArray> listenerResponse = new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        placesFragment.setErrorText("", Color.GREEN);
                        placesFragment.showProgressDialog(context.getResources().getString(R.string.receiving_suggestions_request));

                        //To-Do add to list view
                        placesFragment.setPlacesSuggestionList(translatePlaceJSONArray(response));
                        placesFragment.dismissProgressDialog();
                    }
                };

                Response.ErrorListener listenerError = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            if (error != null) {
                                String errorRes = new String(error.networkResponse.data);
                                JSONObject result = new JSONObject(errorRes);

                                if (result.getInt("status") == 500)
                                    placesFragment.setErrorText("Error : " + result.get("message"), Color.RED);
                                else
                                    placesFragment.setErrorText(context.getResources().getString(R.string.server_error_0), Color.RED);

                                placesFragment.dismissProgressDialog();
                            } else {
                                placesFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                                placesFragment.dismissProgressDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            placesFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                            placesFragment.dismissProgressDialog();
                        }
                    }
                };

                placesFragment.showProgressDialog(context.getResources().getString(R.string.checking_internet));

                if (volleyRequestHandler.hasActiveInternetConnection()) {
                    placesFragment.showProgressDialog(context.getResources().getString(R.string.sending_request));
                    volleyRequestHandler.postRequestWithArrayResult(endpoint + textSearchEndpoint, jsonArrayBody, listenerResponse, listenerError, getToken());
                } else {
                    placesFragment.dismissProgressDialog();
                    placesFragment.setErrorText(context.getResources().getString(R.string.poor_connection), Color.RED);
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    private ArrayList<Place> translatePlaceJSONArray(JSONArray jsonArray){
        ArrayList<Place> placeArrayList = new ArrayList<Place>();

        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                placeArrayList.add(new Place(jsonObject.getString("placeId"),
                        jsonObject.getString("name"),
                        jsonObject.getString("address"),
                        jsonObject.getString("icon"),
                        jsonObject.getLong("rating")));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

        return placeArrayList;
    }

    private JSONArray getInterests(String userId){
        Profile profile = profileLocalDAO.getProfile(userId);
        List<String> interests = new ArrayList<>();

        if(profile != null)
            interests = profile.getLikesList();

        if(interests == null || interests.isEmpty())
            interests.addAll(Arrays.asList("Hotels", "Museum", "History", "Bars", "Restaurant", "Tourist","Attraction"));

        return translateInterest(interests);
    }

    private JSONArray translateInterest(List<String> interests){
        JSONArray jsonArray = new JSONArray();

        for(String interest : interests)
            jsonArray.put(interest);

        return jsonArray;
    }

    public String getUserId(){
        Login login = loginLocalDAO.getLogin();

        if(login == null)
            return null;
        else
            return login.getId();
    }

    private String getToken(){
        Login login = loginLocalDAO.getLogin();

        if(login == null)
            return null;
        else
            return login.getToken();
    }
}
