package com.LVBoxAndroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.LVBoxAndroid.R;
import com.LVBoxAndroid.model.MyFile;

import java.util.ArrayList;

public class FilesAdapter extends ArrayAdapter<MyFile> {

    private ArrayList<MyFile> myFiles;
    private Context context;

    public FilesAdapter(Context context, ArrayList<MyFile> objects) {
        super(context, 0, objects);
        this.context = context;
        this.myFiles = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View _view = convertView;
        FilesAdapter.ViewHolder vh;

        if(myFiles!=null){

            if(_view==null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                _view = inflater.inflate(R.layout.file_list, parent,false);
                vh = new FilesAdapter.ViewHolder();
                vh.fileName = (TextView) _view.findViewById(R.id.tv_file_name);
                _view.setTag(vh);
            }else{
                vh = (FilesAdapter.ViewHolder) _view.getTag();
            }

            MyFile myFile = myFiles.get(position);

            vh.fileName.setText(myFile.getName());

            if(position%2==1){
                _view.setBackgroundResource(R.color.colorPrimary);
            }else{
                _view.setBackgroundResource(R.color.colorAccent);
            }
        }

        return  _view;
    }

    private static class ViewHolder{
        public  TextView fileName;
    }
}
