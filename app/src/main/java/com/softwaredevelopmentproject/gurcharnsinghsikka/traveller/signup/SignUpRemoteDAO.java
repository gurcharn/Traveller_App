package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.signup;

import android.content.Context;
import android.graphics.Color;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.volleyService.VolleyRequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * @author Gurcharn Singh Sikka
 * @version 1.0
 *
 * Object to handle api requests for signup
 */
public class SignUpRemoteDAO {

    private final String endpoint;

    private Context context;
    private VolleyRequestHandler volleyRequestHandler;
    private SignUpActivity signUpActivity;

    /**
     * Constructor
     * @param context
     */
    public SignUpRemoteDAO(Context context) {
        this.endpoint = context.getResources().getString(R.string.signup_endpoint);
        this.context = context;
        this.volleyRequestHandler = new VolleyRequestHandler(context);
        this.signUpActivity = (SignUpActivity) context;
    }

    /**
     * Method to handle signup request with response and error listener
     * @param firstName
     * @param lastName
     * @param email
     * @param username
     * @param password
     */
    public void signUpRequestHandler(final String firstName, final String lastName, final String email, final String username, final String password) {
        android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                JSONObject jsonBody = new JSONObject();
                JSONObject jsonUserInfo = new JSONObject();
                JSONObject jsonLoginInfo = new JSONObject();

                try {
                    jsonUserInfo.put("firstName", firstName);
                    jsonUserInfo.put("lastName", lastName);
                    jsonUserInfo.put("email", email);
                    jsonUserInfo.put("bio", "I love Travelling");
                    jsonUserInfo.put("likes", new JSONArray(Arrays.asList("Hotels", "Museum", "History", "Bars", "Restaurant", "Tourist", "Attraction")));

                    jsonLoginInfo.put("username", username);
                    jsonLoginInfo.put("password", password);

                    jsonBody.put("user", jsonUserInfo);
                    jsonBody.put("login", jsonLoginInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Response.Listener<JSONObject> listenerResponse = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String userId = response.getString("userId");
                            signUpActivity.dismissProgressDialog();

                            String a = context.getResources().getString(R.string.app_name);
                            if (userId == null || userId.isEmpty())
                                signUpActivity.setErrorText(context.getResources().getString(R.string.user_not_registered), Color.RED);
                            else {
                                signUpActivity.resetSignUpForm();
                                signUpActivity.setErrorText(context.getResources().getString(R.string.user_registration_done), Color.GREEN);
                                signUpActivity.snackBar(context.getResources().getString(R.string.user_registration_done));
                                signUpActivity.finishActivity();
                            }
                        } catch (JSONException e) {
                            signUpActivity.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
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
                                    signUpActivity.setErrorText("Error : " + result.get("message"), Color.RED);
                                else
                                    signUpActivity.setErrorText(context.getResources().getString(R.string.server_error_0), Color.RED);

                                signUpActivity.dismissProgressDialog();
                            } else {
                                signUpActivity.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                                signUpActivity.dismissProgressDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            signUpActivity.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                            signUpActivity.dismissProgressDialog();
                        }
                    }
                };

                signUpActivity.showProgressDialog(context.getResources().getString(R.string.checking_internet));

                if (volleyRequestHandler.hasActiveInternetConnection()) {
                    signUpActivity.showProgressDialog(context.getResources().getString(R.string.sending_request));
                    volleyRequestHandler.postRequest(endpoint, jsonBody, listenerResponse, listenerError, null);
                } else {
                    signUpActivity.dismissProgressDialog();
                    signUpActivity.snackBar(context.getResources().getString(R.string.poor_connection));
                }
            }
        };
        mainHandler.post(myRunnable);
    }
}
