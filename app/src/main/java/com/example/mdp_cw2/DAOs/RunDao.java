package com.example.mdp_cw2.DAOs;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mdp_cw2.Entities.GPS;
import com.example.mdp_cw2.Entities.Run;

import java.util.List;

/*
This is the Dao for the Run entries
 */

@Dao
public interface RunDao {

    //insert new netry and return the newly inserted run ID
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Run run);

    //update the existing entry
    @Update
    void update(Run run);

    //delete an entry
    @Delete
    void delete(Run run);

    //get a specific run entry by a given id
    @Query("SELECT * FROM RUN_TABLE WHERE _id = :runID")
    LiveData<Run> getRunById(int runID);

    //get all runs
    @Query("SELECT * FROM RUN_TABLE ORDER BY _id DESC")
    LiveData<List<Run>> getAllRuns();

    //get the run with the highest distance out of all
    @Query("SELECT *, MAX(distance) FROM RUN_TABLE")
    LiveData<Run> getHighestDistance();

    //get the run with the highest pace out of all
    @Query("SELECT *, MIN(pace) FROM RUN_TABLE")
    LiveData<Run> getHighestPace();

    //get the run with the highest distance on todays date
    @Query("SELECT *, MAX(distance) FROM RUN_TABLE WHERE date = :date")
    LiveData<Run> getHighestDistanceToday(String date);

    //get the run with the highest pace on todays date
    @Query("SELECT *, MIN(pace) FROM RUN_TABLE WHERE date = :date")
    LiveData<Run> getHighestPaceToday(String date);

    //return all runs in a cursor, used for content provider
    @Query("SELECT * FROM RUN_TABLE ORDER BY _id DESC")
    Cursor cGetAllRuns();

}
