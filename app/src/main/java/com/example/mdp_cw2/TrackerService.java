package com.example.mdp_cw2;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.mdp_cw2.Database.Repository;
import com.example.mdp_cw2.Entities.GPS;
import com.example.mdp_cw2.Entities.Run;
import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
forground service that tracks the movement and keeps the data locally before pushing it to the db
*/
public class TrackerService extends Service {

    private static final String CHANNEL_ID = "trackerChannel";

    Repository mRepo;

    List<Location> route = new ArrayList<>();
    double distance = 0;
    double time = 0;
    double speed = 0;
    LocationManager locationManager;

    //pointer for how many location updated there has been
    int distancePoint = 0;
    double currentDistance = 0;

    boolean GPSActive;

    //tracking status
    enum Status {TRACKING, PAUSED, STOPPED}

    //start and finish times
    long timeStart = 0;
    long timeFinish;
    Status status;

    Location lastLocation;

    Location[] lastLocations = new Location[2];

    Run run;

    private final IBinder binder = new MyBinder();


    @Override
    public void onCreate() {
        super.onCreate();

        //create new location manager
        locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationTracker locationListener = new LocationTracker();
        //request location updates at every 10m&5ms
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    5, // minimum time interval between updates
                    10, // minimum distance between updates, in metres
                    locationListener);
        } catch (SecurityException e) {
            Log.d("g53mdp", e.toString());
        }

        //return if no permission for location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //get the repo
        mRepo = new Repository( this.getApplication() );

        //create a new notification channel (only for Oreo+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            //create notification manager
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);

        }

        //notification intent, when clicked, go back to the tracking activity (singleton)
        Intent notificationIntent = new Intent(this, TrackingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(TrackerService.this,
                0, notificationIntent, 0);

        //build the foreground notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Running Tracker")
                .setContentText("Currently tracking movement ")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        //start foreground
        startForeground(1, notification); //start foreground so it doesn't get killed*/

        //mRepo.getGPSbyID()

        //mRepo.insert( new Run("86545", "541654", (double) 444444, (double) 555555));
    }

    //only fully stop the service and foreground activity on task removed
    @Override
    public void onTaskRemoved(Intent intent){
        stopForeground(true);
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder {
        TrackerService getService() {
            return TrackerService.this;
        }
    }

    //add the new location
    public void addLocation(Location location){
        route.add(location);

        if( route.size() > 1 ) {
            currentDistance += route.get(distancePoint).distanceTo(route.get(distancePoint+1));
            lastLocations[0] = route.get(distancePoint);
            lastLocations[1] = route.get(distancePoint+1);
            distancePoint++;
        }
    }

    //setter for the service status
    public void setStatus(String newStatus){
        status = Status.valueOf(newStatus);
        if (status == Status.TRACKING){
            if (timeStart == 0) {
                timeStart = System.currentTimeMillis(); //start counter
                run = new Run(mRepo.getCurrentDate(), 0.0, 0.0, 0.0); //make a new run entry
                mRepo.insert(run);
            }
        }
        //if stopped, stop foreground and stop service etc
        if (status == Status.STOPPED){
            timeFinish = System.currentTimeMillis();
            logLocation();
            stopForeground(true);
            stopSelf();
        }
    }

    //log the current location data and update the run entry
    public void logLocation() {

        //calculate the distance by itterating through the location entriy array
        for ( int i = 0; i < route.size() - 1; i++){
            distance += route.get(i).distanceTo(route.get(i+1));
        }

        //Log.d("g53mdp", "--- TIME start: "+ route.get( 0 ).getTime() + " TIME end: " + route.get( route.size()-1 ).getTime());

        //calculate time, speed, pace and set them
        time = (timeFinish - timeStart) / 1000;
        speed = ( distance/time ) * 3.6;
        double pace = time/ (distance/1000);

        run.setTime(time);
        run.setDistance(distance);
        run.setSpeed(speed);
        run.set_id((int)mRepo.getLatestId());
        run.setPace(pace);
        //update the run
        mRepo.update(run);
    }

    //get latLng of the last known location
    public LatLng getLocation() {

        LatLng latLng = new LatLng( Math.round(lastLocation.getLatitude()* 1000.000)/1000.00,
                Math.round(lastLocation.getLongitude() * 1000.000)/1000.00);

        return latLng;
    }

    //get current pace
    public String getCurrentPace() {
        if (distancePoint > 1){
            long time = (lastLocations[1].getTime() - lastLocations[0].getTime())/1000;
            //calculate current pace based on last 2 known locations
            double pace = time/ (lastLocations[0].distanceTo(lastLocations[1])/1000);

            String mPace = String.format("%02d:%02d min/km",
                    TimeUnit.SECONDS.toMinutes((long)pace) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours((long)pace)),
                    TimeUnit.SECONDS.toSeconds((long)pace) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes((long)pace)));
            return mPace;
        }
        else
            return "00:00 min/km";
    }

    //get the current run
    public Run getRun() {
        run.set_id((int)mRepo.getLatestId());
        return run;
    }

    public long getTimeStart(){
        return timeStart;
    }

    public double getDistance() {
        return Math.round((currentDistance/1000) * 100.00)/100.00;
    }

    //location tracked implementation
    public class LocationTracker implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            //on location changed, insert the new location if status = tracking and update last known location
            if(status == Status.TRACKING) {
                Log.d("g53mdp", location.getLatitude() + " " + location.getLongitude());
                mRepo.insert(new GPS((int)mRepo.getLatestId(), location.getTime(), location.getLatitude(), location.getLongitude()));
                lastLocation = location;
                addLocation(location);
            }
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // information about the signal, i.e. number of satellites
            Log.d("g53mdp", "onStatusChanged: " + provider + " " + status);
        }
        @Override
        public void onProviderEnabled(String provider) {
            // the user enabled (for example) the GPS
            GPSActive = true;
            Log.d("g53mdp", "onProviderEnabled: " + provider);
        }
        @Override
        public void onProviderDisabled(String provider) {
            // the user disabled (for example) the GPS
            GPSActive = false;
            Log.d("g53mdp", "onProviderDisabled: " + provider);
            logLocation();
        }

    }
}
