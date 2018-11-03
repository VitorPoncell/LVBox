package com.LVBoxAndroid.activity.Main;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.LVBoxAndroid.model.Contact;
import com.LVBoxAndroid.model.User;
import com.LVBoxAndroid.util.Base64Custom;
import com.LVBoxAndroid.util.FirebaseConfig;
import com.LVBoxAndroid.util.PreferencesCustom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;

public class MainControler {

    private MainActivity activity;
    private DatabaseReference databaseReference;

    public MainControler(MainActivity activity){

        databaseReference = FirebaseConfig.getDatabaseReference();
        this.activity = activity;

        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        File appFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"LVBox");

        if(!appFolder.exists()){
            boolean success = appFolder.mkdirs();
            if(success){
                Log.i("Log","created");
            }else{
                Log.i("Log","error");
            }
        }

        try {
            Log.i("Log",appFolder.getCanonicalPath());
            Log.i("Log",appFolder.getAbsolutePath());
            Log.i("Log",appFolder.getPath());


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addContact(String email){
        final String contactIdentifier = Base64Custom.encodeBase64(email);
        databaseReference = FirebaseConfig.getDatabaseReference().child("users").child(contactIdentifier);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){

                    User contactUser = dataSnapshot.getValue(User.class);

                    PreferencesCustom preferences = new PreferencesCustom(activity);
                    String logedUserIdentifier = preferences.getIdentifier();

                    databaseReference = FirebaseConfig.getDatabaseReference();
                    databaseReference = databaseReference.child("contacts")
                            .child(logedUserIdentifier)
                            .child(contactIdentifier);

                    Contact contact = new Contact();
                    contact.setUserIdentifier(contactIdentifier);
                    contact.setEmail(contactUser.getEmail());
                    contact.setName(contactUser.getName());

                    databaseReference.setValue(contact);

                    databaseReference = FirebaseConfig.getDatabaseReference();
                    databaseReference = databaseReference.child("contacts")
                            .child(contactIdentifier)
                            .child(logedUserIdentifier);

                    contact = new Contact();
                    contact.setUserIdentifier(logedUserIdentifier);
                    contact.setEmail(preferences.getEmail());
                    contact.setName(preferences.getName());

                    databaseReference.setValue(contact);



                }else{
                    activity.showError("Usuario não encontrado");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Erro: ",databaseError.getMessage());
            }
        });
    }

}
