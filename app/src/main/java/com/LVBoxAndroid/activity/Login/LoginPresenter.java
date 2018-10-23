package com.LVBoxAndroid.activity.Login;

import android.widget.EditText;

import com.LVBoxAndroid.model.AuthCustom;
import com.LVBoxAndroid.model.IAuthRecoverListener;
import com.LVBoxAndroid.model.IAuthValidationListener;
import com.LVBoxAndroid.model.User;

public class LoginPresenter implements IAuthValidationListener,IAuthRecoverListener{

    private LoginActivity activity;
    private AuthCustom authCustom;
    private User user;

    public LoginPresenter(LoginActivity loginActivity){
        activity = loginActivity;
        authCustom = new AuthCustom(activity);
    }

    public void login(EditText email, EditText password){

            if(email.getText().toString().isEmpty()||password.getText().toString().isEmpty()){
                activity.showError("Email e senha são obrigatorios");
            }else{
                user = new User();
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());
                authCustom.loginValidation(user,this);
            }
    }

    public void passwordRecover(EditText email){
        if(email.getText().toString().isEmpty()){
            activity.showError("Erro: campo de email vazio");
        }else {
            authCustom.recovery(email.getText().toString(),this);
        }
    }

    @Override
    public void onSuccessLogin() {
        activity.openMain();
    }

    @Override
    public void onErrorLogin(String msg) {
        activity.showError(msg);
    }

    @Override
    public void onSuccessRecover() {
        activity.confirmRecover();
    }

    @Override
    public void onErrorRecover(String msg) {
        activity.showError(msg);
    }
}
