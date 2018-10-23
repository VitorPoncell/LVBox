package com.LVBoxAndroid.model;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.LVBoxAndroid.util.Base64Custom;
import com.LVBoxAndroid.util.FirebaseConfig;
import com.LVBoxAndroid.util.PreferencesCustom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;



public class AuthCustom {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerUser;
    private String userIdentifier;
    private Activity activity;

    public AuthCustom(Activity context){
        this.activity = context;
    }

    public void loginValidation(final User user, final IAuthValidationListener listener) {
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    userIdentifier = Base64Custom.encodeBase64(user.getEmail());

                    databaseReference = FirebaseConfig.getDatabaseReference()
                            .child("users").child(userIdentifier);


                    valueEventListenerUser = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            User userRecover = dataSnapshot.getValue(User.class);

                            if(userRecover == null){

                            }else {
                                PreferencesCustom preferencesCustom = new PreferencesCustom(activity);
                                preferencesCustom.saveData(userIdentifier, userRecover.getName(), userRecover.getEmail());
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    databaseReference.addListenerForSingleValueEvent(valueEventListenerUser);

                    listener.onSuccessLogin();

                } else {
                    String error = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        error = "Usuario ou senha invalidos";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        error = "Usuario ou senha invalidos";
                    } catch (Exception e) {
                        error = "Ao fazer login";
                    }
                    listener.onErrorLogin(error);
                }

            }
        });
    }

    public void recovery(String email, final IAuthRecoverListener listener){
        firebaseAuth = FirebaseConfig.getFirebaseAuth();

            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        listener.onSuccessRecover();
                    } else {
                        listener.onErrorRecover("Erro: ao recuperar senha");
                    }
                }
            });

    }

    public void addUser(final User user, final IAuthSignupListener listener){
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user.setId(Base64Custom.encodeBase64(user.getEmail()));
                    user.save();

                    PreferencesCustom preferencesCustom = new PreferencesCustom(activity);
                    preferencesCustom.saveData(user.getId(),user.getName(),user.getEmail());
                    firebaseAuth.signOut();
                    listener.onSuccessSignup();
                }else{
                    String error = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        error = "Senha fraca!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        error = "Email invalido!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        error = "Email ja em uso!";
                    } catch (Exception e) {
                        error = "Ao efetuar o cadastro!";
                        e.printStackTrace();
                    }

                    listener.onErrorSignup(error);
                }
            }
        });
    }
}
