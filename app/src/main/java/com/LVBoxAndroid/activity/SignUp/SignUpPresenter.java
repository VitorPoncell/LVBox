package com.LVBoxAndroid.activity.SignUp;

import android.widget.EditText;

import com.LVBoxAndroid.model.AuthCustom;
import com.LVBoxAndroid.model.IAuthSignupListener;
import com.LVBoxAndroid.model.User;

public class SignUpPresenter implements IAuthSignupListener{

    private SignUpActivity activity;
    private AuthCustom authCustom;

    public SignUpPresenter(SignUpActivity signUpActivity){
        activity = signUpActivity;
        authCustom = new AuthCustom(activity);
    }

    public void signup(EditText name, EditText email, EditText password1, EditText password2){
        if(name.getText().toString().isEmpty()
                || email.getText().toString().isEmpty()
                || password1.getText().toString().isEmpty()
                || password2.getText().toString().isEmpty()) {
            //erro
        }else{
            if (email.getText().toString().contains("@")) {
                if (password1.getText().toString().equals(password2.getText().toString())) {
                    User user = new User();
                    user.setName(name.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setPassword(password1.getText().toString());
                    authCustom.addUser(user,this);
                } else {
                    //Toast.makeText(SignupActivity.this, "As senhas devem ser iguais",
                            //Toast.LENGTH_LONG).show();
                }
            } else {
                //Toast.makeText(SignupActivity.this, "Email invalido",
                        //Toast.LENGTH_LONG).show();
            }
        }


    }


    @Override
    public void onSuccessSignup() {
        activity.openLogin();
    }

    @Override
    public void onErrorSignup(String msg) {
        activity.showError(msg);
    }
}
