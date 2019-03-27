package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.signup;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        signUpButtonHandler();
        resetFormButtonHandler();
        backToLoginButtonHandler();
    }

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

    private void signUpButtonHandler(){
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFormFilled()){
                    if(isPasswordMatched()){
                        signUpRemoteDAO.signUpRequestHandler(
                                firstName.getText().toString(),
                                lastName.getText().toString(),
                                email.getText().toString(),
                                username.getText().toString(),
                                password.getText().toString()
                        );
                    } else {
                        snackBar("Password not matched");
                    }
                } else{
                    snackBar("Please fill form correctly");
                }
            }
        });
    }

    private void resetFormButtonHandler(){
        resetFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSignUpForm();
            }
        });
    }

    private void backToLoginButtonHandler(){
        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isFormFilled(){
        Boolean formFilled = true;

        if(!isEditTextFilled(firstName))
            formFilled = false;
        if(!isEditTextFilled(lastName))
            formFilled = false;
        if(!isEditTextFilled(email))
            formFilled = false;
        if(!isEditTextFilled(username))
            formFilled = false;
        if(!isEditTextFilled(password))
            formFilled = false;
        if(!isEditTextFilled(confirmPassword))
            formFilled = false;

        return formFilled;
    }

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

    public void snackBar(String message){
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    public void showProgressDialog(String message){
        if(progressDialog.isShowing())
            progressDialog.setMessage(message);
        else{
            progressDialog.setTitle("Signing Up...");
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    public void dismissProgressDialog(){
        progressDialog.dismiss();
    }

    public void setErrorText(String error, int color){
        errorText.setTextColor(color);
        errorText.setText(error);
    }

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
