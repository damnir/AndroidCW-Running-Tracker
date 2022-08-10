package com.example.mdp_cw2.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mdp_cw2.Database.Repository;
import com.example.mdp_cw2.Entities.Run;

import java.util.List;

/*
this is a simple view model for the AllRunsActivity which observer data on allRuns and displays
them in the activity.
 */
public class AllRunsViewModel extends AndroidViewModel {

    //reference to the repo
    private Repository mRepo;
    //for allRuns reference
    LiveData<List<Run>> allRuns;

    public AllRunsViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repository(application);
    }

    //this data is observed by the activity
    public LiveData<List<Run>> getAllRuns(){
        allRuns = mRepo.getAllRuns();
        return allRuns;
    }

    //update the run entry - used to update favourite values
    public void update(Run run) {
        mRepo.update(run);
    }
}
