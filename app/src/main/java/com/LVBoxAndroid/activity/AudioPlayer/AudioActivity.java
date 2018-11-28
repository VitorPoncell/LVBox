package com.LVBoxAndroid.activity.AudioPlayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.LVBoxAndroid.R;

public class AudioActivity extends AppCompatActivity {

    private String path;
    private Button play;
    private TextView name;
    private SeekBar position;
    private SeekBar volume;
    private TextView elapsedTime;
    private TextView remainingTime;
    private MediaPlayer mediaPlayer;
    private int totalTime;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;

            position.setProgress(currentPosition);

            String elapsedTimeValue = createTimeLabel(currentPosition);
            elapsedTime.setText(elapsedTimeValue);

            String remainingTimeValue = createTimeLabel(totalTime-currentPosition);
            remainingTime.setText("- "+remainingTimeValue);
        }
    };

    private String createTimeLabel(int time){
        String timeLabel = "";
        int min = (time /1000) / 60;
        int sec = (time / 1000) % 60;

        timeLabel = min + ":";
        if(sec < 10){
            timeLabel += "0";
        }
        timeLabel += sec;

        return timeLabel;
    };


    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        Intent intent = getIntent();
        if(intent.hasExtra("path") && intent.hasExtra("name")){
            path = intent.getStringExtra("path");
        }else{
            Toast.makeText(this,"Erro ao abrir audio",Toast.LENGTH_LONG).show();
            finish();
        }

        name = (TextView)findViewById(R.id.tv_name);
        play = (Button)findViewById(R.id.btn_play_pause);
        elapsedTime = (TextView)findViewById(R.id.tv_elapsed_time);
        remainingTime = (TextView)findViewById(R.id.tv_remaining_time);
        position = (SeekBar)findViewById(R.id.sb_position);
        volume = (SeekBar)findViewById(R.id.sb_volume);

        name.setText(intent.getStringExtra("name"));
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        }catch (Exception e){
            Log.i("Log: ","erro media player" + e.getMessage() + path);
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);
        mediaPlayer.setVolume(0.5f,0.5f);
        totalTime = mediaPlayer.getDuration();

        position.setMax(totalTime);
        position.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    position.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volumeNum = progress / 100f;
                mediaPlayer.setVolume(volumeNum, volumeNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null){
                    try{
                        Message message = new Message();
                        message.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(message);
                        Thread.sleep(1000);
                    }catch (Exception e){

                    }
                }
            }
        }).start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    play.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                }else{
                    mediaPlayer.start();
                    play.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                }
            }
        });


    }


}
