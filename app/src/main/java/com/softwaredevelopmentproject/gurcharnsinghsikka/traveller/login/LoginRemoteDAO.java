package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.volleyService.VolleyRequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginRemoteDAO {
    private final String ENDPOINT = "login";

    private Context context;
    private LoginLocalDAO loginLocalDAO;
    private VolleyRequestHandler volleyRequestHandler;
    private LoginActivity loginActivity;

    public LoginRemoteDAO(Context context){
        this.context = context;
        this.loginLocalDAO = new LoginLocalDAO(context);
        this.volleyRequestHandler = new VolleyRequestHandler(context);
        this.loginActivity = (LoginActivity) context ;
    }

    public void loginRequestHandler(final String username, final String password){
        android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                JSONObject jsonBody = new JSONObject();

                try{
                    jsonBody.put("username", username);
                    jsonBody.put("password", password);
                } catch (JSONException e){
                    e.printStackTrace();
                }

                Response.Listener<JSONObject> listenerResponse = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Login login = new Login(response.getString("id"), response.getString("username"), response.getString("token"));
                            loginLocalDAO.insertLogin(login);
                            login = loginLocalDAO.getLogin();

                            loginActivity.setErrorText("User ID : " + login.getId());

                            loginActivity.dismissProgressDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Response.ErrorListener listenerError = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try{
                            String errorRes = new String(error.networkResponse.data);
                            JSONObject result = new JSONObject(errorRes);

                            if(result.getInt("status") == 500)
                                loginActivity.setErrorText("Error : " + result.get("message"));
                            else
                                loginActivity.setErrorText("Error : Server Error");

                            loginActivity.dismissProgressDialog();
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };

                loginActivity.showProgressDialog("Checking Internet Connection");

                if(volleyRequestHandler.hasActiveInternetConnection()){
                    loginActivity.showProgressDialog("Authenticating Credentials");
                    volleyRequestHandler.postRequest(ENDPOINT, jsonBody, listenerResponse, listenerError);
                } else{
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
                    loginActivity.dismissProgressDialog();
                }
            }
        };

        mainHandler.post(myRunnable);
    }
}
