package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.chat;

import android.content.Context;
import android.graphics.Color;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login.Login;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login.LoginLocalDAO;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.ProfileLocalDAO;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.volleyService.VolleyRequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatRemoteDAO {

    private final String endpoint;
    private final String chatIdEndpoint;
    private final String userIdEndpoint;

    private Context context;
    private ChatContactFragment chatContactFragment;
    private ChatActivity chatActivity;

    private VolleyRequestHandler volleyRequestHandler;
    private LoginLocalDAO loginLocalDAO;
    private ProfileLocalDAO profileLocalDAO;

    public ChatRemoteDAO(Context context, ChatActivity chatActivity){
        this.endpoint = context.getResources().getString(R.string.chat_endpoint);
        this.chatIdEndpoint = context.getResources().getString(R.string.chatId_endpoint);
        this.userIdEndpoint = context.getResources().getString(R.string.userId_endpoint);
        this.context = context;
        this.chatActivity = chatActivity;
        this.volleyRequestHandler = new VolleyRequestHandler(context);
        this.loginLocalDAO = new LoginLocalDAO(context);
        this.profileLocalDAO = new ProfileLocalDAO(context);
    }

    public ChatRemoteDAO(Context context, ChatContactFragment chatContactFragment){
        this.endpoint = context.getResources().getString(R.string.chat_endpoint);
        this.chatIdEndpoint = context.getResources().getString(R.string.chatId_endpoint);
        this.userIdEndpoint = context.getResources().getString(R.string.userId_endpoint);
        this.context = context;
        this.chatContactFragment = chatContactFragment;
        this.volleyRequestHandler = new VolleyRequestHandler(context);
        this.loginLocalDAO = new LoginLocalDAO(context);
        this.profileLocalDAO = new ProfileLocalDAO(context);
    }

    /**
     *  Method to send fetch chat from server
     */
    public void getChatRequestHandler(final String chatId) {
        android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Response.Listener<JSONObject> listenerResponse = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Chat chat = gson.fromJson(response.toString(), Chat.class);
                        chatActivity.setChat(chat);
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
                                    chatActivity.setErrorText("Error : " + result.get("message"), Color.RED);
                                else
                                    chatActivity.setErrorText(context.getResources().getString(R.string.server_error_0), Color.RED);
                            } else {
                                chatActivity.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            chatActivity.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                        }
                    }
                };

                if (volleyRequestHandler.hasActiveInternetConnection()) {
                    String params = "?chatId=" + chatId;
                    volleyRequestHandler.getRequest(endpoint + chatIdEndpoint + params, listenerResponse, listenerError, getToken());
                } else {
                    chatActivity.setErrorText(context.getResources().getString(R.string.poor_connection), Color.RED);
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    /**
     *  Method to fetch all chats of this user
     */
    public void getAllChatRequestHandler(final String userId) {
        android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Response.Listener<JSONArray> listenerResponse = new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        chatContactFragment.resetContactList(toList(response));
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
                                    chatActivity.setErrorText("Error : " + result.get("message"), Color.RED);
                                else
                                    chatActivity.setErrorText(context.getResources().getString(R.string.server_error_0), Color.RED);
                            } else {
                                chatActivity.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            chatActivity.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                        }
                    }
                };

                if (volleyRequestHandler.hasActiveInternetConnection()) {
                    String params = "?userId=" + userId;
                    volleyRequestHandler.getRequestWithArrayResult(endpoint + userIdEndpoint + params, listenerResponse, listenerError, getToken());
                } else {
                    chatActivity.setErrorText(context.getResources().getString(R.string.poor_connection), Color.RED);
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    /**
     *  Method to create a new or update chat in server
     */
    public void postChatRequestHandler(final Chat chat){
        android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                JSONObject jsonBody = new JSONObject();

                try{
                    jsonBody.put("chatId", chat.getChatId());
                    jsonBody.put("userOne", chat.getUserOne());
                    jsonBody.put("userTwo", chat.getUserTwo());
                    jsonBody.put("messages", toJSONArray(chat.getMessages()));
                } catch (JSONException e){
                    e.printStackTrace();
                }

                Response.Listener<JSONObject> listenerResponse = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Chat updatedChat = gson.fromJson(response.toString(), Chat.class);
                        chatActivity.setChat(updatedChat);
                    }
                };

                Response.ErrorListener listenerError = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try{
                            String errorRes = new String(error.networkResponse.data);
                            JSONObject result = new JSONObject(errorRes);

                            if(result.getInt("status") == 500)
                                chatActivity.setErrorText("Error : " + result.get("message"), Color.RED);
                            else
                                chatActivity.setErrorText(context.getResources().getString(R.string.server_error_0), Color.RED);
                        } catch (JSONException e){
                            e.printStackTrace();
                        } catch (NullPointerException e){
                            e.printStackTrace();
                            chatActivity.setErrorText(context.getResources().getString(R.string.server_error_1), Color.RED);
                        }
                    }
                };

                if(volleyRequestHandler.hasActiveInternetConnection()){
                    volleyRequestHandler.postRequest(endpoint, jsonBody, listenerResponse, listenerError, getToken());
                } else{
                    chatActivity.setErrorText(context.getResources().getString(R.string.poor_connection), Color.RED);
                }
            }
        };

        mainHandler.post(myRunnable);
    }

    /**
     *  Method to conver JSONArray to list of chat
     */
    private List<Chat> toList(JSONArray jsonArray){
        Gson gson = new Gson();
        List<Chat> chatList = new ArrayList<Chat>();

        for(int i=0 ; i<jsonArray.length() ; i++){
            try{
                chatList.add(gson.fromJson(jsonArray.get(i).toString(), Chat.class));
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        return chatList;
    }

    /**
     *  Method to convert messages list to JSONArray
     */
    private JSONArray toJSONArray(List<Message> messages){
        JSONArray jsonArray = new JSONArray();

        for (Message message : messages){
            jsonArray.put(toJSONObject(message));
        }

        return jsonArray;
    }

    /**
     *  Method to convert a message to JSONOBject
     */
    private JSONObject toJSONObject(Message message){
        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.put("senderId", message.getSenderId());
            jsonObject.put("receiverId", message.getRecieverId());
            jsonObject.put("content", message.getContent());
            jsonObject.put("time", message.getTime());
        } catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     *  Method to get userid of current user
     */
    public String getUserId(){
        Login login = loginLocalDAO.getLogin();

        if(login == null)
            return null;
        else
            return login.getId();
    }

    /**
     *  Method to get token of this user
     */
    private String getToken(){
        Login login = loginLocalDAO.getLogin();

        if(login == null)
            return null;
        else
            return login.getToken();
    }


}
