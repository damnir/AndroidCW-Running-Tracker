package com.example.mdp_cw2.Database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.mdp_cw2.DAOs.GPSDao;
import com.example.mdp_cw2.DAOs.RunDao;
import com.example.mdp_cw2.Entities.GPS;
import com.example.mdp_cw2.Entities.Run;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
this is the main repository used to store all queries and access the daos.
 */

public class Repository {

    //references
    private RunDao runDao;
    private GPSDao gpsDao;

    private long new_runID;

    private LiveData<List<GPS>> mGPS;
    private LiveData<List<Run>> allRuns;
    private LiveData<Run> mRun;

    private LiveData<Run> runHighDistance;
    private LiveData<Run> runHighPace;
    private LiveData<Run> runHighDistanceToday;
    private LiveData<Run> runHighPaceToday;


    public Repository(Application application) {
        MainDatabase db = MainDatabase.getDatabase(application);
        //get the daos
        runDao = db.runDao();
        gpsDao = db.gpsDao();
    }

    //insert a new run
    public void insert(Run run) {
        MainDatabase.databaseWriteExecutor.execute(() -> {
            new_runID = runDao.insert(run); });
    }

    //update an existing run entry
    public void update(Run run) {
        MainDatabase.databaseWriteExecutor.execute(() -> {
            runDao.update(run); });
        Log.d("g53mdp", "UPDATED _------------- Distance: " + run.getDistance() + " speed: " + run.getSpeed());
    }

    //insert new gps coordinates
    public void insert(GPS gps) {
        MainDatabase.databaseWriteExecutor.execute(() -> {
            gpsDao.insert(gps); });
    }

    //update existing GPS coordinates
    public void update(GPS gps) {
        MainDatabase.databaseWriteExecutor.execute(() -> {
            gpsDao.update(gps); });
    }

    //get GPS coordinates for a specific run entry
    public LiveData<List<GPS>> getGPSbyID(int runID) {
        mGPS = gpsDao.getAllGPS(runID);
        return mGPS;
    }

    //get all runs
    public LiveData<List<Run>> getAllRuns() {
        allRuns = runDao.getAllRuns();
        return allRuns;
    }

    //get a specific Run using id
    public LiveData<Run> getmRunById(int runID) {
        mRun = runDao.getRunById(runID);
        return mRun;
    }

    //simple function that returns current date as a string in dd:mm:yy
    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        String date = String.format("%s/%s/%s", calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.YEAR));

        Log.d("g53mdp", "Month: " + calendar.get(Calendar.MONTH));
        return date;
    }

    //return the Run with the highest distance
    public LiveData<Run> getHighestDistance() {
        runHighDistance = runDao.getHighestDistance();
        return runHighDistance;
    }

    //return the Run with the highest pace
    public LiveData<Run> getRunHighPace() {
        runHighPace = runDao.getHighestPace();
        return runHighPace;
    }

    //return the Run with the highest distance today
    public LiveData<Run> getHighestDistanceToday() {
        runHighDistanceToday = runDao.getHighestDistanceToday(getCurrentDate());
        return runHighDistanceToday;
    }

    //return the Run with the highest pace today
    public LiveData<Run> getRunHighPaceToday() {
        runHighPaceToday = runDao.getHighestPaceToday(getCurrentDate());
        return runHighPaceToday;
    }

    /*
    public String getHighestPace() {
        Run tempRun = runDao.getHighestPace();
        double pace = tempRun.getPace();
        String mPace = String.format("%02d:%02d",
                TimeUnit.SECONDS.toMinutes((long)pace) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours((long)pace)),
                TimeUnit.SECONDS.toSeconds((long)pace) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes((long)pace)));
        return mPace;
    }*/

    public long getLatestId() {
        return new_runID;
    }

}
