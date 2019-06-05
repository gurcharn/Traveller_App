package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.chat;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.Profile;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.ProfileLocalDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatMessageCustomArrayAdapter extends BaseAdapter {
    private final String myUserId;

    private Context context;
    private List<Message> messageArrayList;
    private ProfileLocalDAO profileLocalDAO;

    public ChatMessageCustomArrayAdapter(Context context, List<Message> messageArrayList, String myUserId) {
        this.myUserId = myUserId;
        this.context = context;
        this.messageArrayList = messageArrayList;
        this.profileLocalDAO = new ProfileLocalDAO(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.chat_item, parent, false);
            viewHolder.firstName = (TextView) convertView.findViewById(R.id.firstName);
            viewHolder.message = (TextView)convertView.findViewById(R.id.message);
            viewHolder.time = (TextView)convertView.findViewById(R.id.time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Message message = (Message) getItem(position);

        viewHolder.firstName.setText(profileLocalDAO.getProfile(message.getSenderId()).getFirstName());
        viewHolder.message.setText(message.getContent());
        viewHolder.time.setText(new Date(message.getTime()).toString());
        viewHolder.time.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

        return convertView;
    }

    private Profile getProfile(Chat chat){
        if(chat.getUserOne() != myUserId)
            return profileLocalDAO.getProfile(chat.getUserOne());
        else
            return profileLocalDAO.getProfile(chat.getUserTwo());
    }

    private void setImage(ImageView imageView, String gender){
        if(gender == null || gender.isEmpty() || gender.equals("male") || gender.equals("Male") || gender.equals("m") || gender.equals("M"))
            imageView.setImageResource(R.drawable.profile_placeholder_male);
        else if(gender.equals("female") || gender.equals("Female") || gender.equals("f") || gender.equals("F"))
            imageView.setImageResource(R.drawable.profile_placeholder_female);
        else
            imageView.setImageResource(R.drawable.profile_placeholder_male);
    }

    private void setTextView(TextView textView, String string){
        String trimString = (string != null ? string : "").trim();
        if(string == null || trimString.isEmpty() || trimString.equals("null") || trimString.equals("null null") || trimString.equals("null n")){
            textView.setVisibility(View.INVISIBLE);
        } else if(string.equals("null M") || string.equals("null m")) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("   M");
        } else if(string.equals("null F") || string.equals("null f")) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("   F");
        } else{
            textView.setVisibility(View.VISIBLE);
            textView.setText(string);
        }
    }

    @Override
    public int getCount(){
        return messageArrayList.size();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position) {
        return messageArrayList.get(position);
    }

    public String getSenderId(int position){
        return messageArrayList.get(position).getSenderId();
    }

    static class ViewHolder {
        public TextView firstName;
        public TextView message;
        public TextView time;
    }
}
