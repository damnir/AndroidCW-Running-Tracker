package com.example.mdp_cw2.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/*
this is an entity (table) for GPS coordinates.
contains a foreign key reference to a run_id to associate coordinates with a run.
 */
@Entity (tableName = "GPS_table", foreignKeys = {@ForeignKey(entity = Run.class,
        parentColumns = "_id",
        childColumns =  "run_id")})

public class GPS {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int _id;

    //specified run id
    @NonNull
    @ColumnInfo(name = "run_id")
    private int run_id;

    //time of the specific location
    @NonNull
    @ColumnInfo(name = "time")
    private double time;

    //latitude value
    @NonNull
    @ColumnInfo(name = "latitude")
    private Double latitude;

    //longitude value
    @NonNull
    @ColumnInfo(name = "longitude")
    private Double longitude;

    //basic constructor
    public GPS(int run_id, double time, Double latitude, Double longitude) {
        this.run_id = run_id;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //getter functions
    public int get_id() { return this._id; }
    public int getRun_id() { return this.run_id; }
    public double getTime() { return this.time; }
    public Double getLatitude() { return this.latitude; }
    public Double getLongitude() { return this.longitude; }

    public void set_id(int id) {
        this._id = id;
    }

}
