package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips;

import android.content.Context;
import android.graphics.Color;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login.Login;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login.LoginLocalDAO;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.volleyService.VolleyRequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TripRemoteDAO {

    private final String endpoint;

    private Context context;
    private NewTripsFragment newTripsFragment;
    private SavedTripsListFragment savedTripsListFragment;
    private EditTripFragment editTripFragment;
    private VolleyRequestHandler volleyRequestHandler;
    private LoginLocalDAO loginLocalDAO;
    private TripLocalDAO tripLocalDAO;

    public TripRemoteDAO(Context context, NewTripsFragment newTripsFragment) {
        this.endpoint = context.getResources().getString(R.string.trip_endpoint);
        this.context = context;
        this.newTripsFragment = (NewTripsFragment) newTripsFragment;
        this.volleyRequestHandler = new VolleyRequestHandler(context);
        this.loginLocalDAO = new LoginLocalDAO(context);
        this.tripLocalDAO = new TripLocalDAO(context);
    }

    public TripRemoteDAO(Context context, SavedTripsListFragment savedTripsListFragment) {
        this.endpoint = context.getResources().getString(R.string.trip_endpoint);
        this.context = context;
        this.savedTripsListFragment = (SavedTripsListFragment) savedTripsListFragment;
        this.volleyRequestHandler = new VolleyRequestHandler(context);
        this.loginLocalDAO = new LoginLocalDAO(context);
        this.tripLocalDAO = new TripLocalDAO(context);
    }

    public TripRemoteDAO(Context context, EditTripFragment editTripFragment) {
        this.endpoint = context.getResources().getString(R.string.trip_endpoint);
        this.context = context;
        this.editTripFragment = (EditTripFragment) editTripFragment;
        this.volleyRequestHandler = new VolleyRequestHandler(context);
        this.loginLocalDAO = new LoginLocalDAO(context);
        this.tripLocalDAO = new TripLocalDAO(context);
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

                        try {
                            Trip trip = new Trip(response.getString("tripId"),
                                                response.getString("userId"),
                                                response.getString("place"),
                                                response.getString("arrival"),
                                                response.getString("departure"));
                            tripLocalDAO.insertTrip(trip);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    public void fetchTripsRequestHandler() {
        android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Response.Listener<JSONArray> listenerResponse = new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Trip> tripArrayList = translateTripJSONArray(response);

                        if(tripArrayList.isEmpty()){
                            return;
                        } else{
                            tripLocalDAO.resetTable();
                            tripLocalDAO.insertTrip(tripArrayList);
                        }
                        savedTripsListFragment.setErrorText("Trip list updated !", Color.GREEN);
                        savedTripsListFragment.dismissProgressDialog();
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
                                    savedTripsListFragment.setErrorText("Error : " + result.get("message"), Color.RED);
                                else
                                    savedTripsListFragment.setErrorText(context.getResources().getString(R.string.server_error_0), Color.RED);
                            } else {
                                savedTripsListFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            savedTripsListFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                        }
                        savedTripsListFragment.dismissProgressDialog();
                    }
                };

                if (volleyRequestHandler.hasActiveInternetConnection()) {
                    String params = "?userId=" + getUserId();
                    volleyRequestHandler.getRequestWithArrayResult(endpoint + params, listenerResponse, listenerError, getToken());
                } else {
                    savedTripsListFragment.dismissProgressDialog();
                    newTripsFragment.setErrorText(context.getResources().getString(R.string.poor_connection), Color.RED);
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    public void updateTripRequestHandler(final Trip trip) {
        android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                JSONObject jsonBody = new JSONObject();

                try {
                    jsonBody.put("tripId", trip.getTripId());
                    jsonBody.put("userId", trip.getUserId());
                    jsonBody.put("place", trip.getPlace());
                    jsonBody.put("arrival", trip.getArrival());
                    jsonBody.put("departure", trip.getDeparture());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Response.Listener<JSONObject> listenerResponse = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        editTripFragment.setErrorText(context.getResources().getString(R.string.update_trip_successfully), Color.GREEN);
                        editTripFragment.dismissProgressDialog();

                        try {
                            Trip trip = new Trip(response.getString("tripId"),
                                    response.getString("userId"),
                                    response.getString("place"),
                                    response.getString("arrival"),
                                    response.getString("departure"));
                            tripLocalDAO.updateTrip(trip);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                                    editTripFragment.setErrorText("Error : " + result.get("message"), Color.RED);
                                else
                                    editTripFragment.setErrorText(context.getResources().getString(R.string.server_error_0), Color.RED);

                                editTripFragment.dismissProgressDialog();
                            } else {
                                editTripFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                                editTripFragment.dismissProgressDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            editTripFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                            editTripFragment.dismissProgressDialog();
                        }
                    }
                };

                editTripFragment.showProgressDialog(context.getResources().getString(R.string.checking_internet));

                if (volleyRequestHandler.hasActiveInternetConnection()) {
                    editTripFragment.showProgressDialog(context.getResources().getString(R.string.sending_request));
                    volleyRequestHandler.updateRequest(endpoint, jsonBody, listenerResponse, listenerError, getToken());
                } else {
                    editTripFragment.dismissProgressDialog();
                    editTripFragment.setErrorText(context.getResources().getString(R.string.poor_connection), Color.RED);
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    public void deleteTripRequestHandler(final Trip trip) {
        android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                JSONObject jsonBody = new JSONObject();

                try {
                    jsonBody.put("tripId", trip.getTripId());
                    jsonBody.put("userId", trip.getUserId());
                    jsonBody.put("place", trip.getPlace());
                    jsonBody.put("arrival", trip.getArrival());
                    jsonBody.put("departure", trip.getDeparture());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Response.Listener<JSONObject> listenerResponse = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Trip trip = new Trip(response.getString("tripId"),
                                    response.getString("userId"),
                                    response.getString("place"),
                                    response.getString("arrival"),
                                    response.getString("departure"));
                            tripLocalDAO.deleteTrip(trip);

                            editTripFragment.dismissProgressDialog();
                            editTripFragment.popBackStack();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                                    editTripFragment.setErrorText("Error : " + result.get("message"), Color.RED);
                                else
                                    editTripFragment.setErrorText(context.getResources().getString(R.string.server_error_0), Color.RED);

                                editTripFragment.dismissProgressDialog();
                            } else {
                                editTripFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                                editTripFragment.dismissProgressDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            editTripFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                            editTripFragment.dismissProgressDialog();
                        }
                    }
                };

                editTripFragment.showProgressDialog(context.getResources().getString(R.string.checking_internet));

                if (volleyRequestHandler.hasActiveInternetConnection()) {
                    editTripFragment.showProgressDialog(context.getResources().getString(R.string.sending_request));
                    volleyRequestHandler.deleteRequest(endpoint, jsonBody, listenerResponse, listenerError, getToken());
                } else {
                    editTripFragment.dismissProgressDialog();
                    editTripFragment.setErrorText(context.getResources().getString(R.string.poor_connection), Color.RED);
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    private ArrayList<Trip> translateTripJSONArray(JSONArray jsonArray){
        ArrayList<Trip> tripArrayList = new ArrayList<Trip>();

        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                tripArrayList.add(new Trip(jsonObject.getString("tripId"),
                                    jsonObject.getString("userId"),
                                    jsonObject.getString("place"),
                                    jsonObject.getString("arrival"),
                                    jsonObject.getString("departure")));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

        return tripArrayList;
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
