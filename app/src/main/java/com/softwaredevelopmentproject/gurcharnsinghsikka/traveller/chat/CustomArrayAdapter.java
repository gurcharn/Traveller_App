package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.Profile;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.ProfileLocalDAO;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.ProfileRemoteDAO;

import java.util.ArrayList;

public class CustomArrayAdapter extends BaseAdapter {
    private final String myUserId;

    private Context context;
    private ArrayList<Chat> chatArrayList;
    private ProfileLocalDAO profileLocalDAO;
    private ProfileRemoteDAO profileRemoteDAO;

    public CustomArrayAdapter(Context context, ChatContactFragment chatContactFragment,ArrayList<Chat> chatArrayList, String myUserId) {
        this.myUserId = myUserId;
        this.context = context;
        this.chatArrayList = chatArrayList;
        this.profileLocalDAO = new ProfileLocalDAO(context);
        this.profileRemoteDAO = new ProfileRemoteDAO(context, chatContactFragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.chat_contact_item, parent, false);
            viewHolder.profileImage = (ImageView) convertView.findViewById(R.id.profileImage);
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Profile profile = getProfile(chatArrayList.get(position));

        try {
            setImage(viewHolder.profileImage, profile.getGender());
            setTextView(viewHolder.name, profile.getFirstName() + " " + profile.getLastName());
        } catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }

    private Profile getProfile(Chat chat){
        if(chat.getUserOne().equals(myUserId))
            return fetchProfile(chat.getUserTwo());
        else
            return fetchProfile(chat.getUserOne());
    }

    private Profile fetchProfile(String userId){
        return profileLocalDAO.getProfile(userId);

//        if(profile == null){
//            profileRemoteDAO.getProfileRequestHandler(userId);
//
//            profile = fetchProfile(userId);
//        }
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
        return chatArrayList.size();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position) {
        return chatArrayList.get(position);
    }

    public String getChatId(int position){
        return chatArrayList.get(position).getChatId();
    }

    static class ViewHolder {
        public ImageView profileImage;
        public TextView name;
    }
}
