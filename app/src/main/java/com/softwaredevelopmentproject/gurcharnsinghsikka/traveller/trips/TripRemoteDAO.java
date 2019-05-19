package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips;

import android.content.Context;
import android.graphics.Color;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login.Login;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login.LoginLocalDAO;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.volleyService.VolleyRequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class TripRemoteDAO {

    private final String endpoint;

    private Context context;
    private NewTripsFragment newTripsFragment;
    private VolleyRequestHandler volleyRequestHandler;
    private LoginLocalDAO loginLocalDAO;

    public TripRemoteDAO(Context context, NewTripsFragment newTripsFragment) {
        this.endpoint = context.getResources().getString(R.string.trip_endpoint);
        this.context = context;
        this.newTripsFragment = (NewTripsFragment) newTripsFragment;
        this.volleyRequestHandler = new VolleyRequestHandler(context);
        this.loginLocalDAO = new LoginLocalDAO(context);
    }

    public void addNewTripRequestHandler(final Trip trip) {
        android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                JSONObject jsonBody = new JSONObject();

                try {
                    jsonBody.put("userId", getUserId());
                    jsonBody.put("place", trip.getPlace());
                    jsonBody.put("arrival", trip.getArrival());
                    jsonBody.put("departure", trip.getDeparture());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Response.Listener<JSONObject> listenerResponse = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        newTripsFragment.setErrorText(context.getResources().getString(R.string.new_trip_added_successfully), Color.GREEN);
                        newTripsFragment.dismissProgressDialog();

//                        try {
//                            String userId = response.getString("userId");
//                            newTripsFragment.dismissProgressDialog();
//
//                            String a = context.getResources().getString(R.string.app_name);
//                            if (userId == null || userId.isEmpty())
//                                newTripsFragment.setErrorText(context.getResources().getString(R.string.user_not_registered), Color.RED);
//                            else {
//                                newTripsFragment.resetSignUpForm();
//                                newTripsFragment.setErrorText(context.getResources().getString(R.string.user_registration_done), Color.GREEN);
//                            }
//                        } catch (JSONException e) {
//                            newTripsFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
//                            e.printStackTrace();
//                        }
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
                                    newTripsFragment.setErrorText("Error : " + result.get("message"), Color.RED);
                                else
                                    newTripsFragment.setErrorText(context.getResources().getString(R.string.server_error_0), Color.RED);

                                newTripsFragment.dismissProgressDialog();
                            } else {
                                newTripsFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                                newTripsFragment.dismissProgressDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            newTripsFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                            newTripsFragment.dismissProgressDialog();
                        }
                    }
                };

                newTripsFragment.showProgressDialog(context.getResources().getString(R.string.checking_internet));

                if (volleyRequestHandler.hasActiveInternetConnection()) {
                    newTripsFragment.showProgressDialog(context.getResources().getString(R.string.sending_request));
                    volleyRequestHandler.postRequest(endpoint, jsonBody, listenerResponse, listenerError, getToken());
                } else {
                    newTripsFragment.dismissProgressDialog();
                    newTripsFragment.setErrorText(context.getResources().getString(R.string.poor_connection), Color.RED);
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    private String getUserId(){
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
