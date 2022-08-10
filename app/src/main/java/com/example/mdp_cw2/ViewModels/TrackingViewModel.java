package com.example.mdp_cw2.ViewModels;

import android.app.Application;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mdp_cw2.Database.Repository;
import com.example.mdp_cw2.Entities.GPS;
import com.example.mdp_cw2.Entities.Run;
import com.example.mdp_cw2.TrackerService;

import java.util.List;
/*
this is a viewmodel for the tracking activity
 */
public class TrackingViewModel extends AndroidViewModel {

    private Repository mRepo;
    //private LiveData<List<GPS>> gpsCord;
    private LiveData<Run> mRun;

    public TrackingViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repository(application);
    }

    //get the latest run entry
    public LiveData<Run> getRun() {
        mRun = mRepo.getmRunById( (int) mRepo.getLatestId() );
        return mRun;
    }

    //insert a new run entry
    public void insert(Run run) {
        mRepo.insert(run);
    }
    //update an existing entry
    public void update(Run run) {
        mRepo.insert(run);
    }
}
