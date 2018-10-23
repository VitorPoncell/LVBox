package com.LVBoxAndroid.model;

public interface IAuthValidationListener {
    public void onSuccessLogin();
    public void onErrorLogin(String msg);
}
