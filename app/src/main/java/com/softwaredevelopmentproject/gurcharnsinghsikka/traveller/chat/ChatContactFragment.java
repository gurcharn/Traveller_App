package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.ProfileRemoteDAO;

import java.util.ArrayList;
import java.util.List;

public class ChatContactFragment extends Fragment {

    private View chatContactView;
    private TextView errorText;
    private TextView refreshButton;
    private ListView contactList;

    private ArrayList<Chat> chatArrayList;
    private ProfileRemoteDAO profileRemoteDAO;
    private ChatRemoteDAO chatRemoteDAO;
    private CustomArrayAdapter customArrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        chatContactView = inflater.inflate(R.layout.chat_contact_layout,container,false);

        init(chatContactView);
        fetchChatContact();
        refreshButtonHandler();
        contactListItemHandler();

        return chatContactView;
    }

    /**
     *  Method to initialise all variables and objects for this class
     */
    private void init(View view){
        errorText = (TextView) view.findViewById(R.id.error_text);
        refreshButton = (TextView) view.findViewById(R.id.refreshButton);
        contactList = (ListView) view.findViewById(R.id.contactList);

        chatArrayList = new ArrayList<Chat>();
        profileRemoteDAO = new ProfileRemoteDAO(getContext(), this);
        chatRemoteDAO = new ChatRemoteDAO(getContext(), this);
        customArrayAdapter = new CustomArrayAdapter(getContext(), this, chatArrayList, profileRemoteDAO.getUserId());
        contactList.setAdapter(customArrayAdapter);
    }

    /**
     *  Method to handle clicks on refresh button
     */
    private void refreshButtonHandler(){
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchChatContact();
            }
        });
    }

    /**
     *  Method to handle clicks on contact list items
     */
    private void contactListItemHandler(){
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startChat(customArrayAdapter.getChatId(position));
            }
        });
    }

    /**
     *  Method to change activity to chat messages activity
     */
    private void startChat(String chatId){
        Intent chatContactActivity = new Intent(getContext() , ChatActivity.class);
        chatContactActivity.putExtra("chatId", chatId);
        startActivityForResult(chatContactActivity, 16);
    }

    /**
     *  Method to fetch recent chats
     */
    private void fetchChatContact(){
        chatRemoteDAO.getAllChatRequestHandler(profileRemoteDAO.getUserId());
    }

    /**
     *  Method to reset contact list of recent chats
     */
    public void resetContactList(List<Chat> chatList){
        chatArrayList.clear();
        for(Chat chat : chatList){
            if(chat.getChatId() != null && !chat.getChatId().equals("null") && !chat.getChatId().equals("")){
                chatArrayList.add(chat);
            }
        }
        customArrayAdapter.notifyDataSetChanged();
    }

    /**
     *  Method to fetch user data of recent chats
     */
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
