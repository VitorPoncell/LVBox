package com.LVBoxAndroid.activity.SignUp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.LVBoxAndroid.R;
import com.LVBoxAndroid.activity.Login.LoginActivity;
import com.LVBoxAndroid.util.Connection;

public class SignUpActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password1;
    private EditText password2;
    private Button signup;

    SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = (EditText)findViewById(R.id.edt_name);
        email = (EditText)findViewById(R.id.edt_email);
        password1 = (EditText)findViewById(R.id.edt_password1);
        password2 = (EditText)findViewById(R.id.edt_password2);
        signup = (Button)findViewById(R.id.btn_signup);

        presenter = new SignUpPresenter(SignUpActivity.this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Connection.verifyNet(SignUpActivity.this)){
                    presenter.signup(name,email,password1,password2);
                }else{
                    showError("No internet connection");
                }
            }
        });
    }

    public void openLogin(){
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void showError(String error){
        Toast.makeText(SignUpActivity.this,error,Toast.LENGTH_LONG).show();
    }
}
