package com.almootassem.android.lab10;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class PlayMedia extends AppCompatActivity {

    Spinner spinner;
    Button play, pause;
    MediaPlayer mediaPlayer;
    SurfaceView sView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_media);

        spinner = (Spinner) findViewById(R.id.spinner);
        play = (Button) findViewById(R.id.play_button);
        pause = (Button) findViewById(R.id.pause_button);
        sView = (SurfaceView) findViewById(R.id.surfaceView1);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.media_names));
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mediaPlayer != null){
                    mediaPlayer.reset();
                    mediaPlayer.setDisplay(null);
                }
                int resID = getResources().getIdentifier(spinner.getSelectedItem().toString(), "raw", getPackageName());
                mediaPlayer = MediaPlayer.create(PlayMedia.this, resID);
                SurfaceHolder holder = sView.getHolder();
                mediaPlayer.setDisplay(holder);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null){
                    mediaPlayer.start();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null){
                    mediaPlayer.pause();
                }
            }
        });
    }
}
