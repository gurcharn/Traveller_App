package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.people;

import android.content.Context;
import android.graphics.Color;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login.Login;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login.LoginLocalDAO;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.Profile;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.ProfileLocalDAO;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.volleyService.VolleyRequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PeopleRemoteDAO {
    private final String endpoint;
    private Context context;
    private VolleyRequestHandler volleyRequestHandler;
    private LoginLocalDAO loginLocalDAO;
    private ProfileLocalDAO profileLocalDAO;
    private PeopleFragment peopleFragment;

    public PeopleRemoteDAO(Context context, PeopleFragment peopleFragment) {
        this.endpoint = context.getResources().getString(R.string.people_endpoint);
        this.context = context;
        this.profileLocalDAO = new ProfileLocalDAO(context);
        this.volleyRequestHandler = new VolleyRequestHandler(context);
        this.loginLocalDAO = new LoginLocalDAO(context);
        this.peopleFragment = peopleFragment;
    }


    public void peopleSuggestionRequestHandler(final String tripId) {
        android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Response.Listener<JSONArray> listenerResponse = new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Profile> profileList = translateProfileJSONArray(response);
                        peopleFragment.setProfileSuggestionList(profileList);
                        peopleFragment.dismissProgressDialog();
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
                                    peopleFragment.setErrorText("Error : " + result.get("message"), Color.RED);
                                else
                                    peopleFragment.setErrorText(context.getResources().getString(R.string.server_error_0), Color.RED);
                            } else {
                                peopleFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            peopleFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                        }
                        peopleFragment.dismissProgressDialog();
                    }
                };

                if (volleyRequestHandler.hasActiveInternetConnection()) {
                    String params = "?tripId=" + tripId;
                    volleyRequestHandler.getRequestWithArrayResult(endpoint + params, listenerResponse, listenerError, getToken());
                } else {
                    peopleFragment.dismissProgressDialog();
                    peopleFragment.setErrorText(context.getResources().getString(R.string.poor_connection), Color.RED);
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    private List<Profile> translateProfileJSONArray(JSONArray jsonArray){
        List<Profile> profileList = new ArrayList<Profile>();

        try{
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if((jsonObject.getString("userId") != null) && !(jsonObject.getString("userId").equals("null"))){
                    Profile profile = new Profile(
                            jsonObject.getString("userId"),
                            jsonObject.getString("firstName"),
                            jsonObject.getString("lastName"),
                            jsonObject.getString("age"),
                            jsonObject.getString("gender"),
                            jsonObject.getString("bio"),
                            jsonObject.getString("email"),
                            jsonObject.getString("phone"),
                            jsonObject.getString("facebook"),
                            translateLikesJSONArray(jsonObject.getJSONArray("likes")));
                    profileLocalDAO.insertProfile(profile);
                    profileList.add(profile);
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        return profileList;
    }

    private List<String> translateLikesJSONArray(JSONArray jsonArray){
        List<String> likes = new ArrayList<>();

        try {
            for(int i = 0; i < jsonArray.length(); i++)
                likes.add(jsonArray.getString(i));
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return likes;
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
