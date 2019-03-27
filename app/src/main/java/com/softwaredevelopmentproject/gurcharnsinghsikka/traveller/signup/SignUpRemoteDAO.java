package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.signup;

import android.content.Context;
import android.graphics.Color;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.volleyService.VolleyRequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpRemoteDAO {

    private final String ENDPOINT = "signUp";

    private Context context;
    private VolleyRequestHandler volleyRequestHandler;
    private SignUpActivity signUpActivity;

    public SignUpRemoteDAO(Context context) {
        this.context = context;
        this.volleyRequestHandler = new VolleyRequestHandler(context);
        this.signUpActivity = (SignUpActivity) context;
    }

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

                            if (userId == null || userId.isEmpty())
                                signUpActivity.setErrorText("User not registered \n Server Error \n Please try again later", Color.RED);
                            else {
                                signUpActivity.resetSignUpForm();
                                signUpActivity.setErrorText("User Registered Successfully", Color.GREEN);
                                signUpActivity.snackBar("User Registered Successfully");
                            }
                        } catch (JSONException e) {
                            signUpActivity.setErrorText("Server Error \n Please try again later", Color.RED);
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
                                    signUpActivity.setErrorText("Error : Server Error", Color.RED);

                                signUpActivity.dismissProgressDialog();
                            } else {
                                signUpActivity.setErrorText("Error : Server Error \n Please try again later", Color.RED);
                                signUpActivity.dismissProgressDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                signUpActivity.showProgressDialog("Checking Internet Connection");

                if (volleyRequestHandler.hasActiveInternetConnection()) {
                    signUpActivity.showProgressDialog("Sending Request");
                    volleyRequestHandler.postRequest(ENDPOINT, jsonBody, listenerResponse, listenerError);
                } else {
                    signUpActivity.dismissProgressDialog();
                    signUpActivity.snackBar("No Internet Connection");
                }
            }
        };
        mainHandler.post(myRunnable);
    }
}
