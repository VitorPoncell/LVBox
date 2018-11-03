package com.LVBoxAndroid.fragment.Contacts;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.LVBoxAndroid.R;
import com.LVBoxAndroid.adapter.ContactsAdapter;
import com.LVBoxAndroid.model.Contact;

import java.util.ArrayList;


public class ContactsFragment extends Fragment {

    private ContactsControler controler;
    private ListView listView;
    private ArrayList<Contact> contacts;
    private ArrayAdapter adapter;

    @Override
    public void onStart() {
        controler.setListener();
        super.onStart();
    }

    @Override
    public void onStop() {
        controler.unsetListener();
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        contacts = new ArrayList<>();
        controler = new ContactsControler(getActivity(),this);

        View view = inflater.inflate(R.layout.fragment_contacts,container,false);

        listView = (ListView) view.findViewById(R.id.lv_contacts);
        adapter = new ContactsAdapter(getActivity(),contacts);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder aleBuilder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogAddContact);
                aleBuilder.setTitle("Excluir contato?");
                aleBuilder.setCancelable(false);

                aleBuilder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Contact contact = contacts.get(position);

                        controler.removeContact(contact);

                    }
                });

                aleBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                aleBuilder.create();
                aleBuilder.show();

                return true;
            }
        });

        return view;
    }

    public void updateContactsList(ArrayList<Contact> contacts){
        this.contacts.clear();
        this.contacts.addAll(contacts);
        adapter.notifyDataSetChanged();
    }


}
