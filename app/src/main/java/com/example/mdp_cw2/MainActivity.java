package com.example.mdp_cw2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mdp_cw2.Entities.Run;
import com.example.mdp_cw2.ViewModels.MainViewModel;

/*
the main activity; basic data about runs is shown and the user can navigate to other activities
from here.
 */
public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    TextView recordDistance;
    TextView recordPace;
    TextView paceToday;
    TextView distanceToday;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //request location permissions
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        //get the instance of the mainviewModel
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MainViewModel.class);


        //observe the highestDistance run liveData and update UI if not null
        viewModel.getHighestDistance().observe(this, run -> {
            if (run.getDistance() != null){
                recordDistance = findViewById(R.id.recordDistance);
                recordDistance.setText(Math.round(run.getDistance()/1000 * 100.00)/100.0 +"km");
            }
        });
        //observe the highestPace run liveData and update UI if not null
        viewModel.getRunHighPace().observe(this, run -> {
            if (run.getPace() != null){
                recordPace = findViewById(R.id.recordPace);
                double pace = run.getPace();
                recordPace.setText(viewModel.formatPace((long) pace) + "\nmin/km");
            }
        });
        //observe the highestDistance today run liveData and update UI if not null
        viewModel.getHighestDistanceToday().observe(this, run -> {
            if (run.getDistance() != null){
                distanceToday = findViewById(R.id.distanceToday);
                distanceToday.setText(Math.round(run.getDistance()/1000 * 100.00)/100.0 +"km");
            }
        });
        //observe the highestPace today run liveData and update UI if not null
        viewModel.getRunHighPaceToday().observe(this, run -> {
            if (run.getPace() != null){
                paceToday = findViewById(R.id.paceToday);
                double pace = run.getPace();
                paceToday.setText(viewModel.formatPace((long) pace) + "\nmin/km");
            }
        });

    }

    public void onTrackClick(View v){
        //if user navigate to the tracking activity, ensure that the app has location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast toast=Toast.makeText(getApplicationContext(),"Allow location permissions for this feature to work",Toast.LENGTH_LONG);
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            toast.show();
            return;
        }
        //start the tracking activity
        Intent intent = new Intent(this, TrackingActivity.class);
        startActivity(intent);
    }


    public void onAllClick(View v) {
        //start teh view all runs activity
        Intent intent = new Intent(this, AllRunsActivity.class);
        startActivity(intent);
    }

}