package com.example.mdp_cw2.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mdp_cw2.Database.Repository;
import com.example.mdp_cw2.Entities.Run;

import java.util.concurrent.TimeUnit;

/*
this is a viewModel for the main activity.
 */
public class MainViewModel extends AndroidViewModel {

    //placeholders for the repo and liveData used in the main activity
    private Repository mRepo;
    LiveData<Run> runHighDistance;
    LiveData<Run> runHighPace;
    LiveData<Run> runHighDistanceToday;
    LiveData<Run> runHighPaceToday;


    public MainViewModel(@NonNull Application application) {
        super(application);

        mRepo = new Repository(application);
    }

    //insert a new entry
    public void insert(Run run) { mRepo.insert(run); }

    public String getDate() { return mRepo.getCurrentDate(); }

    //get the run with the highest distance, function in the repo
    public LiveData<Run> getHighestDistance() {
        runHighDistance = mRepo.getHighestDistance();
        return runHighDistance;
    }

    //get the run with the highest pace, function in the repo
    public LiveData<Run> getRunHighPace() {
        runHighPace = mRepo.getRunHighPace();
        return runHighPace;
    }

    //get the run with the highest distance today, function in the repo
    public LiveData<Run> getHighestDistanceToday() {
        runHighDistanceToday = mRepo.getHighestDistanceToday();
        return runHighDistanceToday;
    }

    //get the run with the highest pace today, function in the repo
    public LiveData<Run> getRunHighPaceToday() {
        runHighPaceToday = mRepo.getRunHighPaceToday();
        return runHighPaceToday;
    }

    //formats a string to display pace as mm:ss using a pace value
    public String formatPace(long pace) {
        String mPace = String.format("%02d:%02d",
                TimeUnit.SECONDS.toMinutes((long)pace) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours((long)pace)),
                TimeUnit.SECONDS.toSeconds((long)pace) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes((long)pace)));
        return mPace;
    }
    /*
    public String getHighestPace() {
        return mRepo.getHighestPace();
    }*/
}
