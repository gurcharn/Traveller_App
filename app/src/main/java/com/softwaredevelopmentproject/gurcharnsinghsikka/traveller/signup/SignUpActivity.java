package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.signup;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;

/**
 * @author Gurcharn Singh Sikka
 * @version 1.0
 *
 * Sign up Activity
 */
public class SignUpActivity extends AppCompatActivity {

    private TextView errorText;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText username;
    private EditText password;
    private EditText confirmPassword;
    private Button signUpButton;
    private Button resetFormButton;
    private Button backToLoginButton;

    private ProgressDialog progressDialog;
    private SignUpRemoteDAO signUpRemoteDAO;

    /**
     * Method to set view when activity created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        signUpButtonHandler();
        resetFormButtonHandler();
        backToLoginButtonHandler();
    }

    /**
     * Method to initialize all variables of this class
     */
    private void init(){
        errorText = (TextView) findViewById(R.id.error_text);
        firstName = (EditText) findViewById(R.id.firstname);
        lastName = (EditText) findViewById(R.id.lastname);
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        signUpButton = (Button) findViewById(R.id.sign_up_button);
        resetFormButton = (Button) findViewById(R.id.reset_form_button);
        backToLoginButton = (Button) findViewById(R.id.back_to_login_button);

        progressDialog = new ProgressDialog(this);
        signUpRemoteDAO = new SignUpRemoteDAO(this);
    }

    /**
     * Method to handle signup button click
     */
    private void signUpButtonHandler(){
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSignUpFormFilled()){
                    signUpRemoteDAO.signUpRequestHandler(
                            firstName.getText().toString(),
                            lastName.getText().toString(),
                            email.getText().toString(),
                            username.getText().toString(),
                            password.getText().toString()
                        );
                } else{
                    snackBar("Please fill form correctly");
                }
            }
        });
    }

    /**
     * Methpod to reset signup form
     */
    private void resetFormButtonHandler(){
        resetFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSignUpForm();
            }
        });
    }

    /**
     * Method to handle back to login button
     */
    private void backToLoginButtonHandler(){
        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Method to check if sign up form is filled properly
     * @return boolean
     */
    private boolean isSignUpFormFilled(){
        Boolean formFilled = true;

        if(!isValidEmail(email.getText().toString()))
            formFilled = false;
        if(!isPasswordMatched())
            formFilled = false;
        if(!isEditTextFilled(confirmPassword))
            formFilled = false;
        if(!isEditTextFilled(password))
            formFilled = false;
        if(!isEditTextFilled(username))
            formFilled = false;
        if(!isEditTextFilled(email))
            formFilled = false;
        if(!isEditTextFilled(lastName))
            formFilled = false;
        if(!isEditTextFilled(firstName))
            formFilled = false;

        return formFilled;
    }

    /**
     * Method to check if edit text field is propely filled.
     * If not filled properly then set error on that field.
     * @param editText
     * @return boolean
     */
    private boolean isEditTextFilled( EditText editText){
        if(editText == null){
            editText.setError("Required");
            editText.requestFocus();
            return false;
        } else if(editText.getText().toString().isEmpty()){
            editText.setError("Required");
            editText.requestFocus();
            return false;
        } else if(editText.getText().toString().contains(" ")){
            editText.setError("Spaces not allowed");
            editText.requestFocus();
            return false;
        } else{
            editText.clearFocus();
            return true;
        }
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
     * Method to check if password and confirm password matched or not
     * Set error if not matched
     * @return boolean
     */
    private boolean isPasswordMatched(){
        if(password.getText().toString().compareTo(confirmPassword.getText().toString()) == 0){
            confirmPassword.setError(null);
            confirmPassword.clearFocus();
            password.setError(null);
            password.clearFocus();
            return true;
        } else{
            confirmPassword.setError("Password not matched");
            confirmPassword.requestFocus();
            password.setError("Password not matched");
            password.requestFocus();
            return false;
        }
    }

    /**
     * Method to show snackbar in activity
     * @param message
     */
    public void snackBar(String message){
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Method to show progress dialog bar
     * @param message
     */
    public void showProgressDialog(String message){
        if(progressDialog.isShowing())
            progressDialog.setMessage(message);
        else{
            progressDialog.setTitle("Signing Up...");
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
     * Method to reset signup form
     */
    public void resetSignUpForm(){
        errorText.setText("");
        firstName.setText("");
        lastName.setText("");
        email.setText("");
        username.setText("");
        password.setText("");
        confirmPassword.setText("");
    }

}
