package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.ProfileRemoteDAO;

import java.util.ArrayList;
import java.util.List;

public class ChatContactActivity extends AppCompatActivity {

    private TextView errorText;
    private TextView refreshButton;
    private ListView contactList;

    private ArrayList<Chat> chatArrayList;
    private ProfileRemoteDAO profileRemoteDAO;
    private ChatRemoteDAO chatRemoteDAO;
    private CustomArrayAdapter customArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_contact_layout);

        init();
        fetchChatContact();
        refreshButtonHandler();
        contactListItemHandler();
    }

    private void init(){
        errorText = (TextView) findViewById(R.id.error_text);
        refreshButton = (TextView) findViewById(R.id.refreshButton);
        contactList = (ListView) findViewById(R.id.contactList);

        chatArrayList = new ArrayList<Chat>();
        profileRemoteDAO = new ProfileRemoteDAO(this, this);
        chatRemoteDAO = new ChatRemoteDAO(this, this);
        customArrayAdapter = new CustomArrayAdapter(this, chatArrayList, profileRemoteDAO.getUserId());
        contactList.setAdapter(customArrayAdapter);
    }

    private void refreshButtonHandler(){
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchChatContact();
            }
        });
    }

    private void contactListItemHandler(){
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startChat(customArrayAdapter.getChatId(position));
            }
        });
    }

    private void startChat(String chatId){
        Intent chatContactActivity = new Intent(ChatContactActivity.this , ChatActivity.class);
        chatContactActivity.putExtra("chatId", chatId);
        startActivityForResult(chatContactActivity, 16);
    }

    private void fetchChatContact(){
        chatRemoteDAO.getAllChatRequestHanlder(profileRemoteDAO.getUserId());
    }

    public void resetContactList(List<Chat> chatList){
        chatArrayList.clear();
        for(Chat chat : chatList){
            if(chat.getChatId() != null && !chat.getChatId().equals("null") && !chat.getChatId().equals("")){
                chatArrayList.add(chat);
            }
        }
//        fetchUsers();
        customArrayAdapter.notifyDataSetChanged();
    }

    private void fetchUsers(){
        for(Chat chat : chatArrayList){
            if(chat.getUserOne().equals(profileRemoteDAO.getUserId()))
                profileRemoteDAO.getProfileRequestHandler(chat.getUserTwo());
            else
                profileRemoteDAO.getProfileRequestHandler(chat.getUserOne());
        }
        customArrayAdapter.notifyDataSetChanged();
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
