package com.LVBoxAndroid.fragment.Files;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.LVBoxAndroid.R;
import com.LVBoxAndroid.adapter.FilesAdapter;
import com.LVBoxAndroid.model.SimpleFileDialog;
import com.LVBoxAndroid.model.MyFile;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;


public class FilesFragment extends Fragment {

    private FloatingActionButton floatingActionButton;
    private FilesControler controler;
    private ArrayList<MyFile> baseMenu;
    private ArrayList<MyFile> list;
    private ListView listView;
    private ArrayAdapter<MyFile> adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files,container,false);

        listView = (ListView)view.findViewById(R.id.lv_files);

        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab_add_file);

        controler = new FilesControler(getActivity(),this);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"add pressed",Toast.LENGTH_LONG).show();

                SimpleFileDialog simpleFileDialog = new SimpleFileDialog(getActivity(), new SimpleFileDialog.SimpleFileDialogListener() {
                    @Override
                    public void onChosenDir(String chosenDir) {
                        Toast.makeText(getActivity(), "Chosen FileOpenDialog File: " +
                                chosenDir, Toast.LENGTH_LONG).show();
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
        baseMenu.add(new MyFile("Images"));
        baseMenu.add(new MyFile("Other"));
        list = new ArrayList<MyFile>();
        adapter = new FilesAdapter(getContext(),list);
        listView.setAdapter(adapter);
        backToBaseMenu();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(list.get(position).getExtension()==null){
                    switch (list.get(position).getName()){
                        case "Audio":
                            controler.getAudioInfo();
                            break;
                        case "..":
                            backToBaseMenu();
                            break;
                    }
                }else{
                    switch (list.get(position).getExtension()){
                        case ".mp3":
                            //open audio
                            break;

                    }
                }
            }
        });

        return view;
    }

    private void backToBaseMenu(){
        list.clear();
        list.addAll(baseMenu);
        adapter.notifyDataSetChanged();
    }

    public void showAudio(ArrayList<MyFile> myFiles){
        list.clear();
        list.add(new MyFile(".."));
        list.addAll(myFiles);
        adapter.notifyDataSetChanged();
    }


}
