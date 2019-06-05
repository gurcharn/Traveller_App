package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile;

import android.content.Context;
import android.graphics.Color;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.chat.ChatActivity;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.chat.ChatContactFragment;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login.Login;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login.LoginLocalDAO;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.volleyService.VolleyRequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileRemoteDAO {

    private final String endpoint;
    private final String updateProfileEndpoint;
    private Context context;
    private VolleyRequestHandler volleyRequestHandler;
    private LoginLocalDAO loginLocalDAO;
    private ProfileLocalDAO profileLocalDAO;
    private ViewProfileFragment viewProfileFragment;
    private EditProfileFragment editProfileFragment;
    private ChatContactFragment chatContactFragment;
    private ChatActivity chatActivity;

    public ProfileRemoteDAO(Context context) {
        this.endpoint = context.getResources().getString(R.string.profile_endpoint);
        this.updateProfileEndpoint = context.getResources().getString(R.string.update_profile_endpoint);
        this.context = context;
        this.profileLocalDAO = new ProfileLocalDAO(context);
        this.volleyRequestHandler = new VolleyRequestHandler(context);
        this.loginLocalDAO = new LoginLocalDAO(context);
    }

    public ProfileRemoteDAO(Context context, ViewProfileFragment viewProfileFragment) {
        this.endpoint = context.getResources().getString(R.string.profile_endpoint);
        this.updateProfileEndpoint = context.getResources().getString(R.string.update_profile_endpoint);
        this.context = context;
        this.profileLocalDAO = new ProfileLocalDAO(context);
        this.volleyRequestHandler = new VolleyRequestHandler(context);
        this.loginLocalDAO = new LoginLocalDAO(context);
        this.viewProfileFragment = viewProfileFragment;
    }

    public ProfileRemoteDAO(Context context, EditProfileFragment editProfileFragment) {
        this.endpoint = context.getResources().getString(R.string.profile_endpoint);
        this.updateProfileEndpoint = context.getResources().getString(R.string.update_profile_endpoint);
        this.context = context;
        this.profileLocalDAO = new ProfileLocalDAO(context);
        this.volleyRequestHandler = new VolleyRequestHandler(context);
        this.loginLocalDAO = new LoginLocalDAO(context);
        this.editProfileFragment = editProfileFragment;
    }

    public ProfileRemoteDAO(Context context, ChatContactFragment chatContactFragment) {
        this.endpoint = context.getResources().getString(R.string.profile_endpoint);
        this.updateProfileEndpoint = context.getResources().getString(R.string.update_profile_endpoint);
        this.context = context;
        this.profileLocalDAO = new ProfileLocalDAO(context);
        this.volleyRequestHandler = new VolleyRequestHandler(context);
        this.loginLocalDAO = new LoginLocalDAO(context);
        this.chatContactFragment = chatContactFragment;
    }

    public ProfileRemoteDAO(Context context, ChatActivity chatActivity) {
        this.endpoint = context.getResources().getString(R.string.profile_endpoint);
        this.updateProfileEndpoint = context.getResources().getString(R.string.update_profile_endpoint);
        this.context = context;
        this.profileLocalDAO = new ProfileLocalDAO(context);
        this.volleyRequestHandler = new VolleyRequestHandler(context);
        this.loginLocalDAO = new LoginLocalDAO(context);
        this.chatActivity = chatActivity;
    }

    public void getMyProfileRequestHandler() {
        android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Response.Listener<JSONObject> listenerResponse = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            Profile profile = new Profile(
                                    response.getString("userId"),
                                    response.getString("firstName"),
                                    response.getString("lastName"),
                                    response.getString("age"),
                                    response.getString("gender"),
                                    response.getString("bio"),
                                    response.getString("email"),
                                    response.getString("phone"),
                                    response.getString("facebook"),
                                    translateLikesJSONArray(response.getJSONArray("likes"))
                            );
                            profileLocalDAO.insertProfile(profile);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Response.ErrorListener listenerError = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                };

                if (volleyRequestHandler.hasActiveInternetConnection()) {
                    String params = "?userId=" + getUserId();
                    volleyRequestHandler.getRequest(endpoint + params, listenerResponse, listenerError, getToken());
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    public void getProfileRequestHandler() {
        android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Response.Listener<JSONObject> listenerResponse = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            Profile profile = new Profile(
                                    response.getString("userId"),
                                    response.getString("firstName"),
                                    response.getString("lastName"),
                                    response.getString("age"),
                                    response.getString("gender"),
                                    response.getString("bio"),
                                    response.getString("email"),
                                    response.getString("phone"),
                                    response.getString("facebook"),
                                    translateLikesJSONArray(response.getJSONArray("likes"))
                            );
                            profileLocalDAO.insertProfile(profile);
                            viewProfileFragment.setProfileDataToView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        viewProfileFragment.dismissProgressDialog();
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
                                    viewProfileFragment.setErrorText("Error : " + result.get("message"), Color.RED);
                                else
                                    viewProfileFragment.setErrorText(context.getResources().getString(R.string.server_error_0), Color.RED);
                            } else {
                                viewProfileFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            viewProfileFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                        }
                        viewProfileFragment.dismissProgressDialog();
                    }
                };

                if (volleyRequestHandler.hasActiveInternetConnection()) {
                    String params = "?userId=" + getUserId();
                    volleyRequestHandler.getRequest(endpoint + params, listenerResponse, listenerError, getToken());
                } else {
                    viewProfileFragment.dismissProgressDialog();
                    viewProfileFragment.setErrorText(context.getResources().getString(R.string.poor_connection), Color.RED);
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    public void getProfileRequestHandler(final String userId) {
        android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Response.Listener<JSONObject> listenerResponse = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            Profile profile = new Profile(
                                    response.getString("userId"),
                                    response.getString("firstName"),
                                    response.getString("lastName"),
                                    response.getString("age"),
                                    response.getString("gender"),
                                    response.getString("bio"),
                                    response.getString("email"),
                                    response.getString("phone"),
                                    response.getString("facebook"),
                                    translateLikesJSONArray(response.getJSONArray("likes"))
                            );
                            profileLocalDAO.insertProfile(profile);
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
                                    chatContactFragment.setErrorText("Error : " + result.get("message"), Color.RED);
                                else
                                    chatContactFragment.setErrorText(context.getResources().getString(R.string.server_error_0), Color.RED);
                            } else {
                                chatContactFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            chatContactFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                        }
                    }
                };

                if (volleyRequestHandler.hasActiveInternetConnection()) {
                    String params = "?userId=" + userId;
                    volleyRequestHandler.getRequest(endpoint + params, listenerResponse, listenerError, getToken());
                } else {
                    chatContactFragment.setErrorText(context.getResources().getString(R.string.poor_connection), Color.RED);
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    public void updateProfileRequestHandler(final Profile profile) {
        android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                JSONObject jsonBody = new JSONObject();

                try {
                    jsonBody.put("userId", profile.getUserId());
                    jsonBody.put("firstName", profile.getFirstName());
                    jsonBody.put("lastName", profile.getLastName());
                    jsonBody.put("age", profile.getAge());
                    jsonBody.put("gender", profile.getGender());
                    jsonBody.put("bio", profile.getBio());
                    jsonBody.put("email", profile.getEmail());
                    jsonBody.put("phone", profile.getPhone());
                    jsonBody.put("facebook", profile.getFacebook());
                    jsonBody.put("likes", translateLikeList(profile.getLikesList()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Response.Listener<JSONObject> listenerResponse = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        editProfileFragment.setErrorText("", Color.GREEN);
                        editProfileFragment.showProgressDialog(context.getResources().getString(R.string.profile_update_request));

                        try {
                            Profile profile = new Profile(
                                    response.getString("userId"),
                                    response.getString("firstName"),
                                    response.getString("lastName"),
                                    response.getString("age"),
                                    response.getString("gender"),
                                    response.getString("bio"),
                                    response.getString("email"),
                                    response.getString("phone"),
                                    response.getString("facebook"),
                                    translateLikesJSONArray(response.getJSONArray("likes")));
                            profileLocalDAO.insertProfile(profile);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        editProfileFragment.dismissProgressDialog();
                        editProfileFragment.popBackStack();
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
                                    editProfileFragment.setErrorText("Error : " + result.get("message"), Color.RED);
                                else
                                    editProfileFragment.setErrorText(context.getResources().getString(R.string.server_error_0), Color.RED);

                                editProfileFragment.dismissProgressDialog();
                            } else {
                                editProfileFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                                editProfileFragment.dismissProgressDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            editProfileFragment.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                            editProfileFragment.dismissProgressDialog();
                        }
                    }
                };

                editProfileFragment.showProgressDialog(context.getResources().getString(R.string.checking_internet));

                if (volleyRequestHandler.hasActiveInternetConnection()) {
                    editProfileFragment.showProgressDialog(context.getResources().getString(R.string.sending_request));
                    volleyRequestHandler.updateRequest(endpoint + updateProfileEndpoint, jsonBody, listenerResponse, listenerError, getToken());
                } else {
                    editProfileFragment.dismissProgressDialog();
                    editProfileFragment.setErrorText(context.getResources().getString(R.string.poor_connection), Color.RED);
                }
            }
        };
        mainHandler.post(myRunnable);
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

    private JSONArray translateLikeList(List<String> likesList){
        JSONArray jsonArray = new JSONArray();

        for(String s : likesList)
            jsonArray.put(s);

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
