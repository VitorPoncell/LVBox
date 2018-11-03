package com.LVBoxAndroid.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.LVBoxAndroid.R;
import com.LVBoxAndroid.model.Contact;

import java.util.ArrayList;

public class ContactsAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> contacts;
    private Context context;

    public ContactsAdapter( Context context, ArrayList<Contact> objects) {
        super(context, 0, objects);
        this.context = context;
        this.contacts = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View _view = convertView;
        ViewHolder vh;

        if(contacts!=null){

            if(_view==null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                _view = inflater.inflate(R.layout.contact_list, parent,false);
                vh = new ViewHolder();
                vh.contactName = (TextView) _view.findViewById(R.id.txt_name);
                vh.contactEmail = (TextView) _view.findViewById(R.id.txt_email);
                _view.setTag(vh);
            }else{
                vh = (ViewHolder) _view.getTag();
            }

            Contact contact = contacts.get(position);

            vh.contactName.setText(contact.getName());
            vh.contactEmail.setText(contact.getEmail());

            if(position%2==1){
                _view.setBackgroundResource(R.color.colorPrimary);
            }else{
                _view.setBackgroundResource(R.color.colorAccent);
            }
        }

        return  _view;
    }

    private static class ViewHolder{
        public  TextView contactName;
        public  TextView contactEmail;
    }
}
