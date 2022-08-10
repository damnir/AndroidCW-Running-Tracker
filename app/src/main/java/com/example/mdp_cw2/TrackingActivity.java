package com.example.mdp_cw2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mdp_cw2.ViewModels.TrackingViewModel;
import com.google.android.gms.maps.GoogleMap;

import java.util.concurrent.TimeUnit;

/*
tracking activity- get updates from the service and update the UI during the tracking
 */
public class TrackingActivity extends AppCompatActivity{

    private TrackingViewModel viewModel;

    private TrackerService trackerService;

    private TextView timeText;
    private TextView distanceText;
    private TextView speedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        //get the viewmodel instance
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(TrackingViewModel.class);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        //on service connected bind to the service and getService
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackerService.MyBinder myBinder = (TrackerService.MyBinder) service;
            trackerService = myBinder.getService();
            trackerService.setStatus("TRACKING"); //start tracking
            progressHandler.postDelayed(updateProgress, 0); //update UI based on progress
            //update(); //call update to update all UI elements
        }
        //on disconnected null the service reference
        @Override
        public void onServiceDisconnected(ComponentName name) {
            trackerService = null;
        }
    };

    private Handler progressHandler = new Handler();
    //runnable, updated every second to give live UI updates
    private Runnable updateProgress = new Runnable() {
        @Override
        public void run() {

            long time = System.currentTimeMillis() - trackerService.getTimeStart();
            //format Time elapsed
            String duration = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(time),
                    TimeUnit.MILLISECONDS.toMinutes(time) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
                    TimeUnit.MILLISECONDS.toSeconds(time) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));

            timeText = findViewById(R.id.time);
            timeText.setText( duration );

            distanceText = findViewById(R.id.distance);
            distanceText.setText(trackerService.getDistance() + "km");

            speedText = findViewById(R.id.speedTrack);
            speedText.setText("Current pace: " + trackerService.getCurrentPace());
            //post a 1 second delay before updating again
            progressHandler.postDelayed(this, 1000);

        }
    };

    public void onButtonClick(View v){
        //start the service and hide buttons, only display stop button
        Intent intent = new Intent(this, TrackerService.class);
        startService(intent);
        this.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        Button start = findViewById(R.id.button5);
        start.setVisibility(v.GONE);
        Button stop = findViewById(R.id.button6);
        stop.setVisibility(v.VISIBLE);
        Button back = findViewById(R.id.button2);
        back.setVisibility(v.GONE);
    }

    public void onButtonStop(View v){
        //on stop, stop the service and runnable
        //run the map activity with the new run
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("runID", trackerService.getRun().get_id());
        trackerService.setStatus("STOPPED");
        progressHandler.removeCallbacks(updateProgress);
        startActivity(intent);
        finish();
    }

    public void onBackClicked(View v) {
        finish();
    }

}