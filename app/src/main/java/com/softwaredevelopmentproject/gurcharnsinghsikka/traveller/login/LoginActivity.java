package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private TextView errorText;
    private Button loginButton;
    private TextView signUpButton;
    private TextView forgotPasswordButton;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        loginButtonHandler();
        signUpButtonHandler();
        forgotPasswordButtonHandler();
    }

    private void init(){
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        errorText = (TextView) findViewById(R.id.error_text);
        loginButton = (Button) findViewById(R.id.login_button);
        signUpButton = (TextView) findViewById(R.id.sign_up_button);
        forgotPasswordButton = (TextView) findViewById(R.id.forgot_password_button);

        progressDialog = new ProgressDialog(this);
    }

    private void loginButtonHandler(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //To-Do check if username and password input is done
                showProgressDialog("Validating Credentials");
                if(isUsernameFilled() && isPasswordFilled()){
                    errorText.setText("");
                    loginRequest();
                } else {
                    errorText.setText("*Please fill Username and Password field properly");
                    dismissProgressDialog();
                }
               //To-Do then send a request a to api
               //T0-Do handle response
            }
        });
    }

    private void signUpButtonHandler(){
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpActivity = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpActivity);
            }
        });
    }

    private void forgotPasswordButtonHandler(){
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To-Do take to new activity for password reset
            }
        });
    }

    private Boolean isUsernameFilled(){
        if(username == null)
            return false;
        else if(username.getText().toString().isEmpty())
            return false;
        else if(username.getText().toString().contains(" "))
            return false;
        else
            return true;
    }

    private Boolean isPasswordFilled(){
        if(password == null)
            return false;
        else if(password.getText().toString().isEmpty())
            return false;
        else if(password.getText().toString().contains(" "))
            return false;
        else
            return true;
    }

    public void setErrorText(String error){
        errorText.setText(error);
    }

    public void loginRequest(){
        LoginRemoteDAO loginRemoteDAO = new LoginRemoteDAO(this);
        loginRemoteDAO.loginRequestHandler(username.getText().toString(), password.getText().toString());
    }

    public void showProgressDialog(String message){
        if(progressDialog.isShowing())
            progressDialog.setMessage(message);
        else{
            progressDialog.setTitle("Logging in...");
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    public void dismissProgressDialog(){
        progressDialog.dismiss();
    }
}
