package com.LVBoxAndroid.activity.VideoPlayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.LVBoxAndroid.R;
import com.LVBoxAndroid.activity.Main.MainActivity;

public class VideoPlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener{

    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;

    private String path;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        surfaceView = (SurfaceView)findViewById(R.id.sv_video_screen);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(VideoPlayerActivity.this);

         Intent intent = getIntent();
         if(intent.hasExtra("path") && intent.hasExtra("name")){
             path = intent.getStringExtra("path");
         }else{
             Toast.makeText(this,"Erro ao abrir audio",Toast.LENGTH_LONG).show();
             finish();
         }

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer!=null){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                    }else{
                        mediaPlayer.start();
                    }
                }
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDisplay(surfaceHolder);
        try{
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(VideoPlayerActivity.this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }catch (Exception e){

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer(){
         if(mediaPlayer!=null){
             mediaPlayer.release();
             mediaPlayer = null;
         }
    }
}
