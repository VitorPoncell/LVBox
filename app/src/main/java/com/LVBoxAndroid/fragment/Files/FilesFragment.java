package com.LVBoxAndroid.fragment.Files;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.LVBoxAndroid.R;
import com.LVBoxAndroid.activity.AudioPlayer.AudioActivity;
import com.LVBoxAndroid.activity.Main.MainActivity;
import com.LVBoxAndroid.activity.VideoPlayer.VideoPlayerActivity;
import com.LVBoxAndroid.adapter.FilesAdapter;
import com.LVBoxAndroid.model.SimpleFileDialog;
import com.LVBoxAndroid.model.MyFile;
import com.LVBoxAndroid.util.BaseUrl;
import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

import yogesh.firzen.filelister.FileListerDialog;
import yogesh.firzen.filelister.OnFileSelectedListener;


public class FilesFragment extends Fragment {

    private FloatingActionButton floatingActionButton;
    private FilesControler controler;
    private ArrayList<MyFile> baseMenu;
    private ArrayList<MyFile> list;
    private ListView listView;
    private ArrayAdapter<MyFile> adapter;
    private String onFoldedr = "base";
    private MainActivity activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files,container,false);

        listView = (ListView)view.findViewById(R.id.lv_files);

        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab_add_file);
        activity = (MainActivity)getActivity();
        controler = new FilesControler(getActivity(),this);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"add pressed",Toast.LENGTH_LONG).show();

                FileListerDialog fileListerDialog = FileListerDialog.createFileListerDialog(getContext());
                fileListerDialog.setOnFileSelectedListener(new OnFileSelectedListener() {
                    @Override
                    public void onFileSelected(File file, String path) {
                        //Toast.makeText(getContext(),file.getName(),Toast.LENGTH_LONG).show();
                        //Toast.makeText(getContext(),path,Toast.LENGTH_LONG).show();
                        controler.uploadFile(path);
                    }
                });
                fileListerDialog.setFileFilter(FileListerDialog.FILE_FILTER.ALL_FILES);
                fileListerDialog.show();
            }
        });

        baseMenu = new ArrayList<>();
        baseMenu.add(new MyFile(".."));
        baseMenu.add(new MyFile("Audio"));
        baseMenu.add(new MyFile("Video"));
        baseMenu.add(new MyFile("Image"));
        baseMenu.add(new MyFile("Other"));
        list = new ArrayList<MyFile>();
        adapter = new FilesAdapter(getContext(),list,this);
        listView.setAdapter(adapter);
        list.addAll(baseMenu);
        adapter.notifyDataSetChanged();
        backToBaseMenu();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(list.get(position).getExtension()==null){
                    switch (list.get(position).getName()){
                        case "Audio":
                            onFoldedr = "audio";
                            controler.getAudioInfo();
                            break;
                        case "Video":
                            onFoldedr = "video";
                            controler.getVideoInfo();
                            break;
                        case "Image":
                            onFoldedr = "image";
                            controler.getImageInfo();
                            break;
                        case "Other":
                            onFoldedr = "other";
                            controler.getOtherInfo();
                            break;
                        case "..":
                            backToBaseMenu();
                            break;
                    }
                }else{
                    Log.i("Log: ","extension " + list.get(position).getExtension());
                    switch (list.get(position).getExtension()){
                        case ".mp3":
                            Log.i("Flag ","toque simples");
                            openAudioActivity(position);
                            break;
                        case ".mp4":
                            Log.i("Flag ","toque simples");
                            openVideoActivity(position);
                            break;
                        case ".png":
                        case ".jpg":
                            openImage(position);
                            Log.i("Flag ","toque simples");
                            //open audio
                            break;
                        default:
                            Log.i("Flag ","toque simples");
                            //n abre
                            break;

                    }
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.AppTheme);
                builder.setTitle("Delete on cloud?");
                builder.setMessage("This will delete the file on cloud and on your device");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        controler.deleteFileAll(list.get(position));
                    }
                });
                builder.create();
                builder.show();
                return true;
            }
        });

        return view;
    }

    private void backToBaseMenu(){
        if(!onFoldedr.equals("base")){
            list.clear();
            list.addAll(baseMenu);
            adapter.notifyDataSetChanged();
            controler.removeEventListener(onFoldedr);
            onFoldedr = "base";
        }
    }

    public void showFiles(ArrayList<MyFile> myFiles){
        list.clear();
        list.add(new MyFile(".."));
        list.addAll(myFiles);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void onStateClick(int position){
        if(list.get(position).getState().equals("local")){
            //exclude local
            controler.deleteLocalFile(list.get(position).getName(),list.get(position).getExtension());
            list.get(position).setState("cloud");
            adapter.notifyDataSetChanged();
        }else if(list.get(position).getState().equals("cloud")){
            //download
            list.get(position).setState("wait");
            adapter.notifyDataSetChanged();
            controler.downloadFile(list.get(position).getPath(), BaseUrl.localPath,list.get(position).getName(),list.get(position).getExtension(),list.get(position).getId());
        }

    }

    private void openAudioActivity(int position){
        if(list.get(position).getState().equals("local")) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            path += File.separator + "LVBox" + File.separator + "audio" + File.separator + list.get(position).getName() + list.get(position).getExtension();
            Intent intent = new Intent(activity, AudioActivity.class);
            intent.putExtra("path", path);
            intent.putExtra("name", list.get(position).getName());
            startActivity(intent);
        }
    }

    private void openVideoActivity(int position){
        if(list.get(position).getState().equals("local")) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            path += File.separator + "LVBox" + File.separator + "video" + File.separator + list.get(position).getName() + list.get(position).getExtension();
            Intent intent = new Intent(activity, VideoPlayerActivity.class);
            intent.putExtra("path", path);
            intent.putExtra("name", list.get(position).getName());
            startActivity(intent);
        }
    }

    private void openImage(int position){
        if(list.get(position).getState().equals("local")) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            path += File.separator + "LVBox" + File.separator + "image" + File.separator + list.get(position).getName() + list.get(position).getExtension();
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(path), "image/");
            activity.startActivity(intent);
        }
    }

    public void showToast(String msg){
        Toast.makeText(activity,msg,Toast.LENGTH_LONG).show();
    }

}
