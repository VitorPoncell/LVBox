package com.LVBoxAndroid.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.LVBoxAndroid.R;
import com.LVBoxAndroid.fragment.Files.FilesFragment;
import com.LVBoxAndroid.model.MyFile;

import java.util.ArrayList;

public class FilesAdapter extends ArrayAdapter<MyFile> {

    private ArrayList<MyFile> myFiles;
    private Context context;
    private FilesFragment fragment;

    public FilesAdapter(Context context, ArrayList<MyFile> objects, FilesFragment fragment) {
        super(context, 0, objects);
        this.context = context;
        this.myFiles = objects;
        this.fragment = fragment;
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
                vh.fileStaveImg = (ImageView) _view.findViewById(R.id.iv_state);
                _view.setTag(vh);
            }else{
                vh = (FilesAdapter.ViewHolder) _view.getTag();
            }


            MyFile myFile = myFiles.get(position);

            vh.fileName.setText(myFile.getName());
            vh.fileStaveImg.clearAnimation();
            if(myFile.getState()!=null){
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
                    if(myFile.getState().equals("cloud")){
                        vh.fileStaveImg.setImageDrawable(context.getDrawable(R.drawable.ic_cloud_download_black_24dp));
                    }else if(myFile.getState().equals("local")){
                        vh.fileStaveImg.setImageDrawable(context.getDrawable(R.drawable.ic_cancel_black_24dp));
                    }else if(myFile.getState().equals("wait")){
                        Log.i("Flag "," adapter wait");
                        vh.fileStaveImg.setImageDrawable(context.getDrawable(R.drawable.ic_access_time_black_24dp));
                    }else if(myFile.getState().equals("downloading")){
                        Log.i("Flag ","adapter download");
                        vh.fileStaveImg.setImageDrawable(context.getDrawable(R.drawable.ic_sync_black_24dp));
                        RotateAnimation animation = new RotateAnimation(360.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        animation.setInterpolator(new LinearInterpolator());
                        animation.setRepeatCount(Animation.INFINITE);
                        animation.setDuration(1500);
                        vh.fileStaveImg.startAnimation(animation);
                    }
                }
                vh.fileStaveImg.setVisibility(View.VISIBLE);
            }else{
                vh.fileStaveImg.setVisibility(View.INVISIBLE);
            }

            vh.fileStaveImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.onStateClick(position);
                }
            });

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
        public ImageView fileStaveImg;
    }
}
