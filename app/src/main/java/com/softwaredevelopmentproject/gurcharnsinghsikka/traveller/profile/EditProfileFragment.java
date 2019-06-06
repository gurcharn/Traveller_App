package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login.LoginLocalDAO;

public class EditProfileFragment extends Fragment {

    private View editProfileView;

    private TextView errorText;
    private EditText firstName;
    private EditText lastName;
    private Spinner gender;
    private Spinner age;
    private EditText bio;
    private EditText email;
    private EditText phone;
    private EditText social;
    private EditText likes;
    private TextView likesList;
    private Button updateButton;
    private Button cancelButton;
    private Button addLikeButton;

    private String ageValue;
    private String genderValue;

    private ProgressDialog progressDialog;

    private ProfileLocalDAO profileLocalDAO;
    private ProfileRemoteDAO profileRemoteDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        editProfileView = inflater.inflate(R.layout.edit_profile_layout,container,false);

        init(editProfileView);
        fetchProfileData();
        ageSpinnerHandler();
        genderSpinnerHandler();
        updateButtonHandler();
        cancelButtonHandler();
        addLikesButtonHandler();

        return editProfileView;
    }

    /**
     *  Method to initialise all variables and objects in this class
     */
    private void init(View view){
        errorText = view.findViewById(R.id.error_text);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        gender = view.findViewById(R.id.gender);
        age = view.findViewById(R.id.age);
        bio = view.findViewById(R.id.bio);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        social = view.findViewById(R.id.social);
        likes = view.findViewById(R.id.likes);
        likesList = view.findViewById(R.id.likesList);
        updateButton = view.findViewById(R.id.updateProfile);
        cancelButton = view.findViewById(R.id.cancelButton);
        addLikeButton = view.findViewById(R.id.addLikeButton);

        ageValue = null;
        genderValue = null;

        progressDialog = new ProgressDialog(this.getContext());
        profileLocalDAO = new ProfileLocalDAO(this.getContext());
        profileRemoteDAO = new ProfileRemoteDAO(this.getContext(), this);
    }

    /**
     *  Method handle spinner for age
     */
    private void ageSpinnerHandler(){
        age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ageValue = parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     *  Method to handle spinner of gender
     */
    private void genderSpinnerHandler(){
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                genderValue = parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     *  Method to handle clicks on update button
     */
    private void updateButtonHandler(){
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isUpdateProfileFormFilled()){
                    showProgressDialog(getResources().getString(R.string.profile_form_check));
                    Profile profile = new Profile(
                            profileRemoteDAO.getUserId(),
                            firstName.getText().toString(),
                            lastName.getText().toString(),
                            getAge(),
                            getGender(),
                            bio.getText().toString(),
                            email.getText().toString(),
                            phone.getText().toString(),
                            social.getText().toString(),
                            likesList.getText().toString()
                    );
                    profileRemoteDAO.updateProfileRequestHandler(profile);
                } else {
                    setErrorText(getResources().getString(R.string.new_trip_form_error), Color.RED);
                }
            }
        });
    }

    /**
     *  Method to handle clicks on cancel button
     */
    private void cancelButtonHandler(){
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    /**
     *  Method to add likes in list
     */
    private void addLikesButtonHandler(){
        addLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newLike = likes.getText().toString();
                String currentLikesList = likesList.getText().toString();

                if(isEditTextFilled(likes)){
                    if(currentLikesList == null)
                        likesList.setText(newLike);
                    else if(currentLikesList.isEmpty())
                        likesList.setText(newLike);
                    else if(currentLikesList.equals("None"))
                        likesList.setText(newLike);
                    else if(currentLikesList.equals(""))
                        likesList.setText(newLike);
                    else if(currentLikesList.trim().equals(""))
                        likesList.setText(newLike);
                    else
                        likesList.setText(currentLikesList + ", " + newLike);

                    likes.setText("");
                }
            }
        });
    }

    /**
     *  Method to check if form is filled
     */
    private boolean isUpdateProfileFormFilled(){
        boolean formFilled = true;

        if(!isEditTextFilled(firstName))
            formFilled = false;
        if(!isEditTextFilled(lastName))
            formFilled = false;
        if(!isValidEmail(email.getText().toString()))
            formFilled = false;
        if(!isValidLikesLIst())
            formFilled = false;
        if(!isValidAge()) {
            formFilled = false;
            age.requestFocus();
        }
        if(!isValidGender()) {
            formFilled = false;
            gender.requestFocus();
        }

        return formFilled;
    }

    /**
     *  Method to check if list of likes is valid
     */
    private boolean isValidLikesLIst(){
        String likes = likesList.getText().toString().trim();

        if(likes == null)
            return false;
        else if(likes.isEmpty())
            return false;
        else if(likes.equals("null"))
            return false;
        else if(likes.equals("None"))
            return false;
        else if(likes.equals("none"))
            return false;
        else
            return true;
    }

    /**
     *  Method to get age
     */
    private String getAge(){
        if(isValidAge())
            return ageValue;
        else
            return null;
    }

    /**
     *  Method to get gender
     */
    private String getGender(){
        if(isValidGender())
            return genderValue;
        else
            return null;
    }

    /**
     *  Method to check if age value is valid
     */
    private boolean isValidAge(){
        if(ageValue == null)
            return false;
        else if(ageValue.isEmpty())
            return false;
        else if(ageValue.equals("null"))
            return false;
        else if(ageValue.equals("Select your age"))
            return false;
        else
            return true;
    }

    /**
     *  Method to check if gender value is valid
     */
    private boolean isValidGender(){
        if(genderValue == null)
            return false;
        else if(genderValue.isEmpty())
            return false;
        else if(genderValue.equals("null"))
            return false;
        else if(genderValue.equals("Select your gender"))
            return false;
        else
            return true;
    }

    /**
     * Method to check if email id is valid or not
     * Set error if not valid
     * @param target
     * @return boolean
     */
    private boolean isValidEmail(CharSequence target) {
        if(TextUtils.isEmpty(target)){
            email.setError("Email ID required");
            email.requestFocus();
            return false;
        } else if(android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()){
            email.setError(null);
            email.clearFocus();
            return true;
        } else{
            email.setError("Invalid Email ID");
            email.requestFocus();
            return false;
        }
    }

    /**
     *  Method to check if edit text field is filled
     */
    private boolean isEditTextFilled(EditText editText){
        String string = editText.getText().toString().trim();

        if(editText == null){
            editText.setError("Required");
            editText.requestFocus();
            return false;
        } else if(string.isEmpty()){
            editText.setError("Required");
            editText.requestFocus();
            return false;
        } else if(string.length() <= 0){
            editText.setError("Required");
            editText.requestFocus();
            return false;
        } else if(string.equals("")){
            editText.setError("Required");
            editText.requestFocus();
            return false;
        } else{
            editText.clearFocus();
            return true;
        }
    }

    /**
     *  Method to fetch profile profile data
     */
    private void fetchProfileData(){
        Profile profile = profileLocalDAO.getProfile(profileRemoteDAO.getUserId());

        if(profile != null){
            setEditText(firstName, profile.getFirstName());
            setEditText(lastName, profile.getLastName());
            setEditText(bio, profile.getBio());
            setEditText(email, profile.getEmail());
            setEditText(phone, profile.getPhone());
            setEditText(social, profile.getFacebook());
            likesList.setText(profile.getLikesString());
            ageValue = profile.getAge();
            genderValue = profile.getGender();
        }
    }

    /**
     *  Method to set edit to text field
     */
    private void setEditText(EditText editText, String text){
        String string = (text != null ? text : "").trim();

        if(editText == null || string == null)
            return;
        else if(string.isEmpty())
            return;
        else if(string.length() <= 0)
            return;
        else if(string.equals(""))
            return;
        else if(string.equals("null"))
            return;
        else
            editText.setText(text);
    }

    /**
     *  Method to close current fragemnt and go back to previous one
     */
    public void popBackStack(){
        getFragmentManager().popBackStack();
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
            progressDialog.setTitle("Trips");
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
