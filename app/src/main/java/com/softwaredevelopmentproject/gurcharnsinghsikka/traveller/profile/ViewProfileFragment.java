package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login.LoginLocalDAO;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips.EditTripFragment;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips.TripLocalDAO;

import java.util.List;

public class ViewProfileFragment extends Fragment {

    private View profileView;
    private TextView logoutButton;
    private TextView refreshButton;
    private TextView editButton;
    private ImageView profileImage;
    private TextView errorText;
    private TextView name;
    private TextView age;
    private TextView bio;
    private TextView email;
    private TextView phone;
    private TextView facebook;
    private TextView likes;

    private ProgressDialog progressDialog;

    private LoginLocalDAO loginLocalDAO;
    private ProfileLocalDAO profileLocalDAO;
    private ProfileRemoteDAO profileRemoteDAO;
    private TripLocalDAO tripLocalDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profileView = inflater.inflate(R.layout.view_profile_layout,container,false);

        init(profileView);
        setProfileDataToView();
        fetchProfile();
        refreshButtonHandler();
        editButtonHandler();
        logoutButtonHandler();

        return profileView;
    }

    private void init(View view){
        logoutButton = view.findViewById(R.id.logout);
        errorText = view.findViewById(R.id.error_text);
        refreshButton = view.findViewById(R.id.refreshButton);
        editButton = view.findViewById(R.id.editButton);
        profileImage = view.findViewById(R.id.profileImage);
        name = view.findViewById(R.id.name);
        age = view.findViewById(R.id.age);
        bio = view.findViewById(R.id.bio);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        facebook = view.findViewById(R.id.social);
        likes = view.findViewById(R.id.likes);

        progressDialog = new ProgressDialog(this.getContext());
        loginLocalDAO = new LoginLocalDAO(this.getContext());
        profileLocalDAO = new ProfileLocalDAO(this.getContext());
        profileRemoteDAO = new ProfileRemoteDAO(this.getContext(), this);
        tripLocalDAO = new TripLocalDAO(this.getContext());
    }

    private void logoutButtonHandler(){
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(getResources().getString(R.string.profile_logout));
                loginLocalDAO.resetTable();
                profileLocalDAO.resetTable();
                tripLocalDAO.resetTable();
                dismissProgressDialog();
                getActivity().finish();
            }
        });
    }

    private void refreshButtonHandler(){
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(getResources().getString(R.string.profile_fetch_request));
                setErrorText("", Color.RED);
                fetchProfile();
            }
        });
    }

    private void editButtonHandler(){
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditProfileFragment();
            }
        });
    }

    private void startEditProfileFragment(){
        EditProfileFragment editProfileFragment = new EditProfileFragment();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.profile_container, editProfileFragment);
        fragmentTransaction.addToBackStack(null).addToBackStack(null).commit();
    }

    private void fetchProfile(){
        profileRemoteDAO.getProfileRequestHandler();
    }

    public void setProfileDataToView(){
        Profile profile = profileLocalDAO.getProfile(profileRemoteDAO.getUserId());
        if(profile != null){
            setProfilePic(profileImage, profile.getGender());
            setTextView(name, profile.getFirstName() + " " + profile.getLastName());
            setTextView(age, profile.getAge() + " " + profile.getGender());
            setTextView(bio, profile.getBio());
            setTextView(email, profile.getEmail());
            setTextView(phone, profile.getPhone());
            setTextView(facebook, profile.getFacebook());
            setTextView(likes, profile.getLikesString());
        } else {
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

    private void setProfilePic(ImageView imageView, String gender){
        if(gender == null || gender.isEmpty() || gender.equals("male") || gender.equals("Male") || gender.equals("m") || gender.equals("M"))
            imageView.setImageResource(R.drawable.profile_placeholder_male);
        else if(gender.equals("female") || gender.equals("Female") || gender.equals("f") || gender.equals("F"))
            imageView.setImageResource(R.drawable.profile_placeholder_female);
        else
            imageView.setImageResource(R.drawable.profile_placeholder_male);
    }

    private void setTextView(TextView textView, String string){
        String trimString = (string != null ? string : "").trim();
        if(string == null || trimString.isEmpty() || trimString.equals("null") || trimString.equals("null null")){
            textView.setVisibility(View.INVISIBLE);
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

    /**
     * Method to show progress dialog box
     * @param message
     */
    public void showProgressDialog(String message){
        if(progressDialog.isShowing())
            progressDialog.setMessage(message);
        else{
            progressDialog.setTitle("Profile");
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    /**
     * Method to dismiss progress dialog
     */
    public void dismissProgressDialog(){
        progressDialog.dismiss();
    }
}
