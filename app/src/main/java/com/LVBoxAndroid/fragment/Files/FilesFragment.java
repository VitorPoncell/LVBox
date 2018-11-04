package com.LVBoxAndroid.fragment.Files;

import android.os.Bundle;
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

import com.LVBoxAndroid.R;
import com.LVBoxAndroid.activity.Main.MainActivity;
import com.LVBoxAndroid.adapter.FilesAdapter;
import com.LVBoxAndroid.model.SimpleFileDialog;
import com.LVBoxAndroid.model.MyFile;
import com.LVBoxAndroid.util.BaseUrl;
import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;


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

                SimpleFileDialog simpleFileDialog = new SimpleFileDialog(getActivity(), new SimpleFileDialog.SimpleFileDialogListener() {
                    @Override
                    public void onChosenDir(String chosenDir) {
                        controler.uploadFile(chosenDir);
                    }
                });

                simpleFileDialog.Default_File_Name = "";
                simpleFileDialog.chooseFile_or_Dir();
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
                    switch (list.get(position).getExtension()){
                        case ".mp3":
                            Log.i("Flag ","toque simples");
                            //open audio
                            break;
                        case ".mp4":
                            Log.i("Flag ","toque simples");
                            //open audio
                            break;
                        case ".png":
                        case ".jpg":
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
                    Log.i("Flag ","toque long");
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

}
