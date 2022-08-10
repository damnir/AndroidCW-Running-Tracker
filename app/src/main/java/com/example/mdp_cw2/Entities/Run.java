package com.example.mdp_cw2.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


/*
this  is an entity (table) for logging runs/walks.
columns can be seen below
 */
@Entity(tableName = "run_table")
public class Run {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int _id;

    //date of the run
    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    //duration of the run
    @NonNull
    @ColumnInfo(name = "time")
    private Double time;

    //user satisfaction (default 0) ranges from 1-3
    @NonNull
    @ColumnInfo(name = "satisfaction")
    private int satisfaction;

    //distance ran
    @NonNull
    @ColumnInfo(name = "distance")
    private Double distance;

    @NonNull
    @ColumnInfo(name = "speed")
    private Double speed;

    //pace min/kmh
    @ColumnInfo(name = "pace")
    private Double pace;

    //any notes about the run
    @ColumnInfo(name = "note")
    private String note;

    //can be marked as favourite
    @ColumnInfo(name = "favourite")
    private boolean favourite;

    public Run(String date, Double time, Double distance, Double speed) {
        this.date = date;
        this.time = time;
        this.distance = distance;
        this.speed = speed;
        this.favourite = false;
    }

    //getter and setter methods
    public int get_id() { return this._id; }
    public String getDate() { return this.date; }
    public Double getTime() { return this.time; }
    public int getSatisfaction() { return this.satisfaction; }
    public Double getDistance() { return this.distance; }
    public Double getSpeed() { return this.speed; }
    public String getNote() { return this.note; }
    public Double getPace() { return this.pace; }
    public boolean getFavourite() { return this.favourite; }

    public void setNote(String note) {
        this.note = note;
    }

    public void set_id(int id) {
        this._id = id;
    }
    public void setTime(double time){
        this.time = time;
    }
    public void setDistance(double distance){
        this.distance = distance;
    }
    public void setSatisfaction(int satisfaction) { this.satisfaction = satisfaction; }
    public void setSpeed(double speed){
        this.speed = speed;
    }
    public void setPace(Double pace){
        this.pace = pace;
    }
    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
