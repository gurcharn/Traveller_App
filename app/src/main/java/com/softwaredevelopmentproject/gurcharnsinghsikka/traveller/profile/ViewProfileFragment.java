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
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips.EditTripFragment;

import java.util.List;

public class ViewProfileFragment extends Fragment {

    private View profileView;
    private TextView refreshButton;
    private TextView editButton;
    private TextView errorText;
    private TextView name;
    private TextView age;
    private TextView bio;
    private TextView email;
    private TextView facebook;
    private TextView likes;

    private ProgressDialog progressDialog;

    private ProfileLocalDAO profileLocalDAO;
    private ProfileRemoteDAO profileRemoteDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profileView = inflater.inflate(R.layout.view_profile_layout,container,false);

        init(profileView);
        setProfileDataToView();
        fetchProfile();
        refreshButtonHandler();
        editButtonHandler();

        return profileView;
    }

    private void init(View view){
        errorText = view.findViewById(R.id.error_text);
        refreshButton = view.findViewById(R.id.refreshButton);
        editButton = view.findViewById(R.id.editButton);
        name = view.findViewById(R.id.name);
        age = view.findViewById(R.id.age);
        bio = view.findViewById(R.id.bio);
        email = view.findViewById(R.id.email);
        facebook = view.findViewById(R.id.social);
        likes = view.findViewById(R.id.likes);

        progressDialog = new ProgressDialog(this.getContext());
        profileLocalDAO = new ProfileLocalDAO(this.getContext());
        profileRemoteDAO = new ProfileRemoteDAO(this.getContext(), this);
    }

    private void refreshButtonHandler(){
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(getResources().getString(R.string.profile_fetch_request));
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
            name.setText(profile.getFirstName() + " " + profile.getLastName());
            age.setText(profile.getAge() + " " + profile.getGender());
            bio.setText(profile.getBio());
            email.setText(profile.getEmail());
            facebook.setText(profile.getFacebook());
            likes.setText(profile.getLikesString());
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
