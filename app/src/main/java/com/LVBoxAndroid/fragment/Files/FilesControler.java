package com.LVBoxAndroid.fragment.Files;

import android.app.Activity;
import android.util.Log;

import com.LVBoxAndroid.model.MyFile;
import com.LVBoxAndroid.util.BaseUrl;
import com.LVBoxAndroid.util.FirebaseConfig;
import com.LVBoxAndroid.util.PreferencesCustom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import TransactionCustom.ITransactionListener;
import TransactionCustom.TransactionControler;

public class FilesControler {

    private Activity activity;
    private FilesFragment fragment;
    private ValueEventListener audioEventListener;
    private ValueEventListener videoEventListener;
    private ValueEventListener imageEventListener;
    private ValueEventListener otherEventListener;
    private DatabaseReference databaseReference;
    private PreferencesCustom preferencesCustom;
    private ArrayList<MyFile> myFiles;
    private TransactionControler transactionControler;

    public FilesControler(Activity activity, FilesFragment fragment){
        this.activity = activity;
        this.fragment = fragment;
        preferencesCustom = new PreferencesCustom(activity);
        myFiles = new ArrayList<MyFile>();
        databaseReference = FirebaseConfig.getDatabaseReference().child("files").child(preferencesCustom.getIdentifier());
        transactionControler = TransactionControler.getInstance(2);
    }

    public void getAudioInfo(){
        audioEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myFiles.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    MyFile myFile = data.getValue(MyFile.class);
                    myFile.setId(data.getKey());
                    myFiles.add(myFile);
                }
                verifyLocalFile("audio");
                fragment.showFiles(myFiles);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.child("audio").addValueEventListener(audioEventListener);
    }

    public void getVideoInfo(){
        videoEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myFiles.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    MyFile myFile = data.getValue(MyFile.class);
                    myFile.setId(data.getKey());
                    myFiles.add(myFile);
                }
                verifyLocalFile("video");
                fragment.showFiles(myFiles);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.child("video").addValueEventListener(videoEventListener);
    }

    public void getImageInfo(){
        imageEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myFiles.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    MyFile myFile = data.getValue(MyFile.class);
                    myFile.setId(data.getKey());
                    myFiles.add(myFile);
                }
                verifyLocalFile("image");
                fragment.showFiles(myFiles);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.child("image").addValueEventListener(imageEventListener);
    }

    public void getOtherInfo(){
        otherEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myFiles.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    MyFile myFile = data.getValue(MyFile.class);
                    myFile.setId(data.getKey());
                    myFiles.add(myFile);
                }
                verifyLocalFile("other");
                fragment.showFiles(myFiles);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.child("other").addValueEventListener(otherEventListener);
    }

    public void removeEventListener(String local){
        switch (local){
            case "audio":
                databaseReference.child(local).removeEventListener(audioEventListener);
                break;
            case "video":
                databaseReference.child(local).removeEventListener(videoEventListener);
                break;
        }
    }


    public void uploadFile(String chosenFile){
        MyFile myFile = new MyFile();
        String[] str = chosenFile.split("/");
        String completeName = str[str.length-1];
        String[] fileName = completeName.split("\\.");
        myFile.setName(fileName[0]);
        myFile.setExtension("."+fileName[1]);
        //myFile.setVersion();
        myFile.setAlive("True");
        myFile.setOwner(preferencesCustom.getIdentifier());
        String type = "";
        switch (fileName[1]){
            case "mp3":
                type = "audio";
                break;
            case "mp4":
                type = "video";
                break;
            case "jpg":
            case "png":
                type = "image";
                break;
            default:
                type = "other";
                break;


        }
        String url = BaseUrl.storageUrlBegin.concat(preferencesCustom.getIdentifier()).concat(BaseUrl.storageUrlFolderSeparator).concat(type).concat(BaseUrl.storageUrlFolderSeparator).concat(completeName.replace(" ",BaseUrl.storageUrlSpace)).concat(BaseUrl.storageUrlEnd);
        myFile.setPath(url);
        String fileId = databaseReference.child(type).push().getKey();
        moveFile(completeName,chosenFile,BaseUrl.localPath+"/"+type+"/");

        databaseReference.child(type).child(fileId).setValue(myFile);
        //upar no storage
        transactionControler.addUploadTask(url, BaseUrl.localPath+"/"+type+"/"+completeName, fileId, new ITransactionListener() {
            @Override
            public void onInit(String s) {

            }

            @Override
            public void onClomplete(String s, String s1) {
                Log.i("Flag: ","sucsess upload of " + s);
                for(MyFile myFile:myFiles){
                    if(myFile.getId().equals(fileId)){
                        myFile.setState("local");
                        Log.i("Flag ","local");
                    }
                }
                fragment.showFiles(myFiles);
            }

            @Override
            public void onError(String s, String s1) {
                Log.i("Flag: ","erro");
            }
        });

    }

    public void downloadFile(String url,String localPath, String name,String extension, String id){
        localPath += File.separator;
        switch (extension){
            case ".mp3":
                localPath +="audio";
                break;
            case ".mp4":
                localPath += "video";
                break;
            case ".jpg":
            case ".png":
                localPath += "image";
                break;
            default:
                localPath += "other";
                break;
        }
        name+=extension;
        Log.i("Flag ",url);
        Log.i("Flag ",localPath);
        Log.i("Flag ",name);
        Log.i("Flag ",id);

        transactionControler.addDownlaodTask(url, localPath, name, id, new ITransactionListener() {
            @Override
            public void onInit(String s) {
                Log.i("Flag ","init");
                for(MyFile myFile:myFiles){
                    if(myFile.getId().equals(id)){
                        myFile.setState("downloading");
                    }
                }
                fragment.showFiles(myFiles);
            }

            @Override
            public void onClomplete(String s, String s1) {
                Log.i("Flag ","done");
                for(MyFile myFile:myFiles){
                    if(myFile.getId().equals(id)){
                        myFile.setState("local");
                    }
                }
                fragment.showFiles(myFiles);
            }

            @Override
            public void onError(String s, String s1) {
                Log.i("Flag ","erro download");
                for(MyFile myFile:myFiles){
                    if(myFile.getId().equals(id)){
                        myFile.setState("cloud");
                    }
                }
                fragment.showFiles(myFiles);
            }
        });
    }

    private void verifyLocalFile(String type){
        for (MyFile myFile: myFiles) {
            File file = new File(BaseUrl.localPath+File.separator+type+File.separator+myFile.getName()+myFile.getExtension());
            try{Log.i("Flag: ",file.getCanonicalPath());}catch (Exception e){

            }
            if(file.exists()){
                myFile.setState("local");
            }else{
                myFile.setState("cloud");
            }
        }
    }

    private void moveFile(String fileName,String oldFile, String newPath){
        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (newPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(oldFile);
            out = new FileOutputStream(newPath + fileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();

            //new File(oldFile).delete();


        }catch (Exception e){

        }

    }

    public void deleteLocalFile(String fileName, String fileExtension){
        String type;
        switch (fileExtension){
            case ".mp3":
                type = "audio";
                break;
            case ".mp4":
                type = "video";
                break;
            default:
                type = "other";
                break;
        }
        File file = new File(BaseUrl.localPath+File.separator+type+File.separator+fileName+fileExtension);
        if(file.exists()){
            file.delete();
        }else{
            //erro arquivo n encontrado
        }
    }
}
