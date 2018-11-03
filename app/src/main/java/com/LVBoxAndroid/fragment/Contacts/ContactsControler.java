package com.LVBoxAndroid.fragment.Contacts;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;

import com.LVBoxAndroid.model.Contact;
import com.LVBoxAndroid.util.FirebaseConfig;
import com.LVBoxAndroid.util.PreferencesCustom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContactsControler {

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private ArrayList<Contact> contacts;
    private Activity activity;
    private ContactsFragment fragment;
    private PreferencesCustom preferencesCustom;

    public ContactsControler(Activity activity, final ContactsFragment fragment){
        contacts = new ArrayList<>();

        this.activity = activity;
        this.fragment = fragment;
        this.preferencesCustom = new PreferencesCustom(activity);
        PreferencesCustom preferencesCustom = new PreferencesCustom(activity);
        String logedUserId = preferencesCustom.getIdentifier();

        databaseReference = FirebaseConfig.getDatabaseReference().child("contacts").child(logedUserId);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contacts.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Contact contact = data.getValue(Contact.class);
                    contacts.add(contact);
                }
                fragment.updateContactsList(contacts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }

    public void removeContact(Contact contact){
        databaseReference = FirebaseConfig.getDatabaseReference();

        databaseReference = databaseReference.child("contacts")
                .child(preferencesCustom.getIdentifier())
                .child(contact.getUserIdentifier());
        try {
            databaseReference.removeValue();
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }

        databaseReference = FirebaseConfig.getDatabaseReference();

        databaseReference = databaseReference.child("contacts")
                .child(contact.getUserIdentifier())
                .child(preferencesCustom.getIdentifier());
        try {
            databaseReference.removeValue();
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }


    }

    public void setListener(){
        databaseReference.addValueEventListener(valueEventListener);
    }

    public void unsetListener(){
        databaseReference.removeEventListener(valueEventListener);
    }
}
