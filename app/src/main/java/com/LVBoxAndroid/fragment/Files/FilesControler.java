package com.LVBoxAndroid.fragment.Files;

import android.app.Activity;
import android.util.Log;

import com.LVBoxAndroid.model.MyFile;
import com.LVBoxAndroid.util.FirebaseConfig;
import com.LVBoxAndroid.util.PreferencesCustom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.security.Timestamp;
import java.util.ArrayList;

public class FilesControler {

    private Activity activity;
    private FilesFragment fragment;
    private ValueEventListener audioEventListener;
    private DatabaseReference databaseReference;
    private PreferencesCustom preferencesCustom;
    private ArrayList<MyFile> myFiles;

    public FilesControler(Activity activity, FilesFragment fragment){
        this.activity = activity;
        this.fragment = fragment;
        preferencesCustom = new PreferencesCustom(activity);
        myFiles = new ArrayList<MyFile>();
        databaseReference = FirebaseConfig.getDatabaseReference().child("files").child(preferencesCustom.getIdentifier());
    }

    public void getAudioInfo(){
        audioEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myFiles.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    MyFile myFile = data.getValue(MyFile.class);
                    myFiles.add(myFile);
                }
                fragment.showAudio(myFiles);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.child("audio").addValueEventListener(audioEventListener);
    }

    public void uploadFile(String chosenFile){
        MyFile myFile = new MyFile();
        String[] str = chosenFile.split("/");
        String completeName = str[str.length-1];
        String[] fileName = completeName.split("\\.");
        myFile.setName(fileName[0]);
        myFile.setExtension("."+fileName[1]);
        //myFile.setVersion();
        //myFile.setPath();
        myFile.setAlive("True");
        myFile.setOwner(preferencesCustom.getIdentifier());
        String type = "";
        switch (fileName[1]){
            case "mp3":
                type = "audio";
                break;


        }
        String fileId = databaseReference.child(type).push().getKey();
        databaseReference.child(type).child(fileId).setValue(myFile);
        //upar no storage
    }
}
