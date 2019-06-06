package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.people;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.Profile;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.ProfileRemoteDAO;

import java.util.ArrayList;

public class CustomArrayAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Profile> profileArrayList;
    private ProfileRemoteDAO profileRemoteDAO;

    public CustomArrayAdapter(Context context, ArrayList<Profile> profileArrayList) {
        this.context = context;
        this.profileArrayList = profileArrayList;
        this.profileRemoteDAO = new ProfileRemoteDAO(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.people_list_item, parent, false);
            viewHolder.profileImage = (ImageView) convertView.findViewById(R.id.profileImage);
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.age = (TextView)convertView.findViewById(R.id.age);
            viewHolder.bio = (TextView)convertView.findViewById(R.id.bio);
            viewHolder.interests = (TextView)convertView.findViewById(R.id.interests);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Profile profile = profileArrayList.get(position);
        profileRemoteDAO.getProfileRequestHandler(profile.getUserId());

        setImage(viewHolder.profileImage, profile.getGender());
        setTextView(viewHolder.name, profile.getFirstName() + " " + profile.getLastName());
        setTextView(viewHolder.age, profile.getAge() + " " + profile.getGender().charAt(0));
        setTextView(viewHolder.bio, profile.getBio());
        setTextView(viewHolder.interests, profile.getLikesString());

        return convertView;
    }

    /**
     *  Method to set image of suggested profile
     */
    private void setImage(ImageView imageView, String gender){
        if(gender == null || gender.isEmpty() || gender.equals("male") || gender.equals("Male") || gender.equals("m") || gender.equals("M"))
            imageView.setImageResource(R.drawable.profile_placeholder_male);
        else if(gender.equals("female") || gender.equals("Female") || gender.equals("f") || gender.equals("F"))
            imageView.setImageResource(R.drawable.profile_placeholder_female);
        else
            imageView.setImageResource(R.drawable.profile_placeholder_male);
    }

    /**
     *  Method to set data in text view
     */
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
        return profileArrayList.size();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position) {
        return profileArrayList.get(position);
    }

    /**
     *  Method to get user id
     */
    public String getUserId(int position){
        return profileArrayList.get(position).getUserId();
    }

    static class ViewHolder {
        public ImageView profileImage;
        public TextView name;
        public TextView age;
        public TextView bio;
        public TextView interests;
    }
}
