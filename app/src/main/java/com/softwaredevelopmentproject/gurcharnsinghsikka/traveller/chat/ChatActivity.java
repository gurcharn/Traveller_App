package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.Profile;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.ProfileLocalDAO;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.ProfileRemoteDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class ChatActivity extends AppCompatActivity {

    private TextView errorText;
    private TextView refreshButton;
    private TextView name;
    private ListView messageListView;
    private EditText messageEditor;
    private Button sendButton;

    private Chat chat;
    private List<Message> messages;
    private Profile myProfile;
    private Profile otherProfile;
    private ChatRemoteDAO chatRemoteDAO;
    private ProfileLocalDAO profileLocalDAO;
    private ProfileRemoteDAO profileRemoteDAO;
    private ChatMessageCustomArrayAdapter chatMessageCustomArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        init();
        getIntentValue();
        refreshButtonHandler();
        nameClickHandler();
        sendButtonClickHandler();
        updateChat();
    }

    private void init(){
        errorText = (TextView) findViewById(R.id.error_text);
        refreshButton = (TextView) findViewById(R.id.refreshButton);
        name = (TextView) findViewById(R.id.firstName);
        messageListView = (ListView) findViewById(R.id.messageList);
        messageEditor = (EditText) findViewById(R.id.messageEditor);
        sendButton = (Button) findViewById(R.id.sendButton);

        messages = new ArrayList<Message>();
        chatRemoteDAO = new ChatRemoteDAO(this, this);
        profileLocalDAO = new ProfileLocalDAO(this);
        profileRemoteDAO = new ProfileRemoteDAO(this, this);
        myProfile = profileLocalDAO.getProfile(profileRemoteDAO.getUserId());
        chatMessageCustomArrayAdapter = new ChatMessageCustomArrayAdapter(this, messages, myProfile.getUserId());
        messageListView.setAdapter(chatMessageCustomArrayAdapter);
    }

    private void refreshButtonHandler(){
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatRemoteDAO.getChatRequestHandler(chat.getChatId());
            }
        });
    }

    private void sendButtonClickHandler(){
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = messageEditor.getText().toString();
                String trimText = (text != null ? text : "").trim();

                if(!trimText.isEmpty()){
                    Message message = new Message(myProfile.getUserId(), otherProfile.getUserId(), text);
                    messages.add(message);
                    chat.setMessages(messages);
                    chatRemoteDAO.postChatRequestHandler(chat);
                    messageEditor.setText("");
                }
            }
        });
    }

    private void nameClickHandler(){
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getIntentValue(){
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String chatId = intent.getStringExtra("chatId");

        if(userId!=null && !userId.equals("")){
            Chat newChat = new Chat(null, profileRemoteDAO.getUserId(), userId, new ArrayList<Message>());
            chatRemoteDAO.postChatRequestHandler(newChat);
        } else {
            chatRemoteDAO.getChatRequestHandler(chatId);
        }
    }

    public void setChat(Chat chat){
        this.chat = chat;

        if(chat.getUserOne().equals(myProfile.getUserId()))
            setOtherProfile(profileLocalDAO.getProfile(chat.getUserTwo()));
        else
            setOtherProfile(profileLocalDAO.getProfile(chat.getUserOne()));

        setDataToView();
    }

    private void setOtherProfile(Profile otherProfile){
        this.otherProfile = otherProfile;
    }

    private void setDataToView(){
        try{
            name.setText(otherProfile.getFirstName());
            setMessages(chat.getMessages());
            chatMessageCustomArrayAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setMessages(List<Message> newMessages){
        messages.clear();
        messages.addAll(newMessages);
    }

    private void updateChat(){
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(chat.getChatId()!=null)
                    chatRemoteDAO.getChatRequestHandler(chat.getChatId());

                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    /**
     * Method to set error text and its color in view
     * @param error
     * @param color
     */
    public void setErrorText(String error, int color){
        errorText.setTextColor(color);
        errorText.setText(error);
    }
}
