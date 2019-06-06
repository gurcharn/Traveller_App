package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.people;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.chat.ChatActivity;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.Profile;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.ProfileLocalDAO;

public class PeopleViewFragment extends Fragment {

    private View peopleView;
    private ImageView profileImage;
    private Button messageButton;
    private TextView errorText;
    private TextView name;
    private TextView age;
    private TextView bio;
    private TextView email;
    private TextView phone;
    private TextView facebook;
    private TextView likes;

    private ProfileLocalDAO profileLocalDAO;
    private Profile profile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        peopleView = inflater.inflate(R.layout.people_view_profile_layout,container,false);

        init(peopleView);
        fetchProfile();
        setProfileDataToView();
        messageButtonHandler();

        return peopleView;
    }

    /**
     *  Method to initialise all varibales and objects of this class
     */
    private void init(View view){
        errorText = view.findViewById(R.id.error_text);
        profileImage = view.findViewById(R.id.profileImage);
        messageButton = (Button) view.findViewById(R.id.messageButton);
        name = view.findViewById(R.id.name);
        age = view.findViewById(R.id.age);
        bio = view.findViewById(R.id.bio);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        facebook = view.findViewById(R.id.social);
        likes = view.findViewById(R.id.likes);

        profileLocalDAO = new ProfileLocalDAO(this.getContext());
    }

    /**
     *  Method to handler clicks on message button
     */
    private void messageButtonHandler(){
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChatContactActivity(profile.getUserId());
            }
        });
    }

    /**
     *  Method to fetch profile
     */
    private void fetchProfile(){
        Bundle bundle = getArguments();
        profile = profileLocalDAO.getProfile(bundle.getString("userId"));
    }

    /**
     *  Method to set profile data to layout
     */
    public void setProfileDataToView(){
        if(profile != null){
            setProfilePic(profileImage, profile.getGender());
            setTextView(name, profile.getFirstName() + " " + profile.getLastName());
            setTextView(age, profile.getAge() + " " + profile.getGender().charAt(0));
            setTextView(bio, profile.getBio());
            setTextView(email, profile.getEmail());
            setTextView(phone, profile.getPhone());
            setTextView(facebook, profile.getFacebook());
            setTextView(likes, profile.getLikesString());
        } else {
            setErrorText("Error : Profile Not found", Color.RED);
            setProfilePic(profileImage, null);
            setTextView(name, null);
            setTextView(age, null);
            setTextView(bio, null);
            setTextView(email, null);
            setTextView(phone, null);
            setTextView(facebook, null);
            setTextView(likes, null);
        }
    }

    /**
     *  Method to start chat contacts activity
     */
    private void startChatContactActivity(String userId){
        Intent chatContactActivity = new Intent(this.getContext() , ChatActivity.class);
        chatContactActivity.putExtra("userId", userId);
        startActivityForResult(chatContactActivity, 18);
    }

    /**
     *  Method to set profile pic
     */
    private void setProfilePic(ImageView imageView, String gender){
        if(gender == null || gender.isEmpty() || gender.equals("male") || gender.equals("Male") || gender.equals("m") || gender.equals("M"))
            imageView.setImageResource(R.drawable.profile_placeholder_male);
        else if(gender.equals("female") || gender.equals("Female") || gender.equals("f") || gender.equals("F"))
            imageView.setImageResource(R.drawable.profile_placeholder_female);
        else
            imageView.setImageResource(R.drawable.profile_placeholder_male);
    }

    /**
     *  Method to set text view
     */
    private void setTextView(TextView textView, String string){
        String trimString = (string != null ? string : "").trim();
        if(string == null || trimString.isEmpty() || trimString.equals("null") || trimString.equals("null null") || trimString.equals("null n")){
            textView.setVisibility(View.INVISIBLE);
        } else if(string.equals("null M") || string.equals("null m")) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("M");
        } else if(string.equals("null F") || string.equals("null f")) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("F");
        } else{
            textView.setVisibility(View.VISIBLE);
            textView.setText(string);
        }
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
