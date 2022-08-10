package com.example.mdp_cw2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mdp_cw2.Entities.GPS;
import com.example.mdp_cw2.Entities.Run;
import com.example.mdp_cw2.R;
import com.example.mdp_cw2.ViewModels.MapViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
this activity shows information about a specific run;
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    //ui placeholders
    private MapViewModel viewModel;

    private Run run;

    private TextView distanceText;
    private TextView durationText;
    private TextView avgSpeedText;
    private TextView avgPaceText;
    private EditText editNotes;

    private Button satisfation1;
    private Button satisfation2;
    private Button satisfation3;

    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //get the runID
        Bundle extras = getIntent().getExtras();
        int runID = extras.getInt("runID");

        //find the Google Maps map fragment in the activity
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //get the MapViewmodel instance
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MapViewModel.class);

        //observe the GPS data -> add coordinates to a list and draw the route
        viewModel.getGpsCord(runID).observe(this, GPS -> {
            viewModel.addToGPSList(GPS);
            drawRoute(viewModel.getGPSList(), viewModel.getLatLngList());
            });

        //observe the run data -> update UI
        viewModel.getRun(runID).observe(this, newRun -> {
            run = newRun;
            setText();
            updateSatisfaction();
        });

    }

    //this functions updates all UI elements
    private void setText() {
        distanceText = findViewById(R.id.distanceText);
        durationText = findViewById(R.id.durationText);
        avgSpeedText = findViewById(R.id.speedText);
        avgPaceText = findViewById(R.id.paceText);
        editNotes = findViewById(R.id.editNotes);

        double time = run.getTime();

        //format the run duration
        String duration = viewModel.formatTime( (long) time);

        //calculate pace
        double pace = run.getTime()/ (run.getDistance()/1000);
        //format pace
        String mPace = viewModel.formatPace((long)pace);

        double distance = Math.round((run.getDistance()/1000) * 100.00)/100.0;
        double speed = Math.round(run.getSpeed() * 100.00)/100.0;
        //update all ui text
        distanceText.setText("Distance: " + distance+"km");
        durationText.setText("Duration: " + duration);
        avgSpeedText.setText("Avg. Speed: " + speed + " km/h");
        avgPaceText.setText(("Avg. Pace: " + mPace + " min/km"));
        editNotes.setText(run.getNote());
    }

    //this functions draws the route on the Google Map fragment
    void drawRoute(List<GPS> GPSList, List<LatLng> latLngList) {

        //get the coordinates for the initial position
        double v = Math.round(GPSList.get(0).getLatitude() * 1000.000)/1000.00;
        double v1 =  Math.round(GPSList.get(0).getLongitude() * 1000.000)/1000.00;

        //move the camera to the initial position
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(v, v1), 15);
        //CameraUpdate cameraUpdate1 = CameraUpdateFactory.newLatLngBounds()
        googleMap.moveCamera(cameraUpdate);

        //add polylines to represent the route
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(false)
                .addAll(latLngList));

        polyline1.setColor(Color.RED);
        polyline1.setWidth(20);
        polyline1.setJointType(JointType.ROUND);

        //add marker for origin
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(GPSList.get(0).getLatitude(), GPSList.get(0).getLongitude()))
                .title("Start"));

        //add marker for destination
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(GPSList.get(GPSList.size()-1).getLatitude(), GPSList.get(GPSList.size()-1).getLongitude()))
                .title("Finish"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    //on afinish button click, update the run note and close the activity
    public void onFinishClick(View v) {
        EditText notes = (EditText) findViewById(R.id.editNotes);
        run.setNote(notes.getText().toString());
        viewModel.update(run);
        finish();
    }

    //update run satisfaction and update the active button based on run satisfaction
    public void updateSatisfaction() {
        satisfation1 = findViewById(R.id.satisfation1);
        satisfation2 = findViewById(R.id.satisfaction2);
        satisfation3 = findViewById(R.id.satisfaction3);

        if(run.getSatisfaction() != 0) {
            switch (run.getSatisfaction()){
                case 1:
                    satisfation1.setBackgroundColor(Color.rgb(98,0,238));
                    satisfation2.setBackgroundColor(0x00000000);
                    satisfation3.setBackgroundColor(0x00000000);
                    break;
                case 2:
                    satisfation2.setBackgroundColor(Color.rgb(98,0,238));
                    satisfation1.setBackgroundColor(0x00000000);
                    satisfation3.setBackgroundColor(0x00000000);
                    break;
                case 3:
                    satisfation3.setBackgroundColor(Color.rgb(98,0,238));
                    satisfation1.setBackgroundColor(0x00000000);
                    satisfation2.setBackgroundColor(0x00000000);
            }
        }

        satisfation1.setOnClickListener(view -> {
            satisfation1.setBackgroundColor(Color.rgb(98,0,238));
            satisfation2.setBackgroundColor(0x00000000);
            satisfation3.setBackgroundColor(0x00000000);
            run.setSatisfaction(1); viewModel.update(run);
        });

        satisfation2.setOnClickListener(view -> {
            satisfation2.setBackgroundColor(Color.rgb(98,0,238));
            satisfation1.setBackgroundColor(0x00000000);
            satisfation3.setBackgroundColor(0x00000000);
            run.setSatisfaction(2); viewModel.update(run);
        });

        satisfation3.setOnClickListener(view -> {
            satisfation3.setBackgroundColor(Color.rgb(98,0,238));
            satisfation1.setBackgroundColor(0x00000000);
            satisfation2.setBackgroundColor(0x00000000);
            run.setSatisfaction(3); viewModel.update(run);
        });
    }
}