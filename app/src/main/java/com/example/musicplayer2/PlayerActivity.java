package com.example.musicplayer2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    Button btn_next, btn_previous, btn_pause;
    TextView songTextlabel;
    SeekBar songSeekbar;
    static MediaPlayer myMediaPlayer;
    TextView elapsedTimelabel;
    TextView remainingTimelabel;
    int position;
    String sname;
    ArrayList<File> mySongs;
    Thread updateseekBar;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        ImageView animas = findViewById(R.id.music);

        Glide.with(this).load(R.drawable.gif).into(animas);

        btn_next = (Button) findViewById(R.id.next);
        btn_previous = (Button) findViewById(R.id.previous);
        btn_pause = (Button) findViewById(R.id.pause);
        songTextlabel = (TextView) findViewById(R.id.songlabel);
        remainingTimelabel=(TextView)findViewById(R.id.remainingTimelabel);


        //positioBar
        songSeekbar=(SeekBar)findViewById(R.id.seekbar);
        songSeekbar.setMax(position);
        songSeekbar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser){
                            myMediaPlayer.seekTo(progress);
                            songSeekbar.setProgress(progress);
                        }

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );



        updateseekBar = new Thread() {


            @Override
            public void run() {

                int totaDuration = myMediaPlayer.getDuration();
                int currentposition = 0;


                while (currentposition < totaDuration) {

                    try {

                        sleep(500);
                        currentposition = myMediaPlayer.getCurrentPosition();
                        songSeekbar.setProgress(currentposition);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        if (myMediaPlayer != null) {
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");

        sname = mySongs.get(position).getName().toString();

        final String songName = i.getStringExtra("songname");

        songTextlabel.setText(songName);
        songTextlabel.setSelected(true);

        position = bundle.getInt("pos", 0);

        Uri u = Uri.parse(mySongs.get(position).toString());


        myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);

        myMediaPlayer.start();
        songSeekbar.setMax(myMediaPlayer.getDuration());

        updateseekBar.start();

        songSeekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        songSeekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                myMediaPlayer.seekTo(seekBar.getProgress());


            }

        });


        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                songSeekbar.setMax(myMediaPlayer.getDuration());

                if (myMediaPlayer.isPlaying()) {


                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    myMediaPlayer.pause();

                } else {
                    btn_pause.setBackgroundResource(R.drawable.icon_pause);
                    myMediaPlayer.start();
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myMediaPlayer.stop();
                myMediaPlayer.release();
                position = ((position + 1) % mySongs.size());

                Uri u = Uri.parse((mySongs.get(position).toString()));

                myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);

                sname = mySongs.get(position).getName().toString();
                songTextlabel.setText(sname);


                myMediaPlayer.start();

            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myMediaPlayer.stop();
                myMediaPlayer.release();

                position = ((position - 1) < 0) ? (mySongs.size() - 1) : (position - 1);
                Uri u = Uri.parse(mySongs.get(position).toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);

                sname = mySongs.get(position).getName().toString();
                songTextlabel.setText(sname);


                myMediaPlayer.start();
            }



            private Handler handler= new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        int currentPosition=msg.what;
                        //update position,
                        songSeekbar.setProgress(currentPosition);

                        //update labels.
                        String elapsedTime =createTimeLabel(currentPosition);
                        elapsedTimelabel.setText(elapsedTime);


                        String remainingTime = createTimeLabel(position-currentPosition);
                        remainingTimelabel.setText("- "  + remainingTime);

                    }
                };



            public String createTimeLabel(int time){
                String timeLabel = "";
                int min= time / 1000 / 60;
                int sec =time/ 1000 % 60;

                timeLabel=min + ":";
                if (sec < 10) timeLabel += "0";
                timeLabel+= sec;

                return  timeLabel;

            }

        });
    }
}