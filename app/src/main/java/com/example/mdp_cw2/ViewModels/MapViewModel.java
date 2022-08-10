package com.example.mdp_cw2.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mdp_cw2.Database.Repository;
import com.example.mdp_cw2.Entities.GPS;
import com.example.mdp_cw2.Entities.Run;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
this is a viewmodel for the map/run viewving activity
 */
public class MapViewModel extends AndroidViewModel {

    private Repository mRepo;
    private LiveData<List<GPS>> gpsCord;
    private LiveData<Run> mRun;
    private List<GPS> GPSList; //a list of GPS entries used in the activity
    private List <LatLng> latLngList; //a list of latLngs used to draw out the route


    public MapViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repository(application);
    }

    //public void insert(Run run) { mRepo.insert(run); }
    public LiveData<List<GPS>> getGpsCord(int _id) {
        gpsCord = mRepo.getGPSbyID(_id);
        return gpsCord;
    }

    //get a specific run using id
    public LiveData<Run> getRun(int _id) {
        mRun = mRepo.getmRunById(_id);
        return mRun;
    }

    //update run entry
    public void update(Run run) {
        mRepo.update(run);
    }

    public List<GPS> getGPSList() {
        return  GPSList;
    }

    public List<LatLng> getLatLngList() {
        return latLngList;
    }

    //fills the GPS and latLng lists
    public void addToGPSList(List<GPS> list) {
        if (GPSList != null) {
            GPSList.clear();
            GPSList.addAll(list);
        } else {
            GPSList = list;
        }

        latLngList = new ArrayList<LatLng>();

        for ( GPS gps : GPSList) {
            latLngList.add(new LatLng(gps.getLatitude(), gps.getLongitude()));
        }
    }

    //simple function to format duration of a run
    public String formatTime(long time) {
        String duration = String.format("%02d:%02d:%02d",
                TimeUnit.SECONDS.toHours((long)time),
                TimeUnit.SECONDS.toMinutes((long)time) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours((long)time)),
                TimeUnit.SECONDS.toSeconds((long)time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes((long)time)));
        return duration;
    }

    //simple function to format pace of a run
    public String formatPace(long pace) {
        String mPace = String.format("%02d:%02d",
                TimeUnit.SECONDS.toMinutes((long)pace) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours((long)pace)),
                TimeUnit.SECONDS.toSeconds((long)pace) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes((long)pace)));
        return mPace;
    }

}
