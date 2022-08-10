package com.example.mdp_cw2.DAOs;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mdp_cw2.Entities.GPS;
import com.example.mdp_cw2.Entities.Run;

import java.util.List;

/*
This is the Dao for the GPS table that contains simple Queries
 */

@Dao
public interface GPSDao {

    //insert new GPS coordinates and in case of conflict, ignore
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(GPS gps);

    //update an existing entry
    @Update
    void update(GPS gps);

    //select all gps coordinates for a given run entry
    @Query("SELECT * FROM GPS_table WHERE run_id = :runID ORDER BY _id")
    LiveData<List<GPS>> getAllGPS(int runID);

    //return all GPS coordinates in a cursor, used by the content provider
    @Query("SELECT * FROM GPS_table ORDER BY _id")
    Cursor cGetAllGPS();

}
