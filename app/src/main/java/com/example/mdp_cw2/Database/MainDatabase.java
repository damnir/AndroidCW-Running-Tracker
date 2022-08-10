package com.example.mdp_cw2.Database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mdp_cw2.DAOs.GPSDao;
import com.example.mdp_cw2.DAOs.RunDao;
import com.example.mdp_cw2.Entities.GPS;
import com.example.mdp_cw2.Entities.Run;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
this is the main room database.
 */
@Database(entities = {Run.class, GPS.class}, version = 2) //run and gps classes as entities
public abstract class MainDatabase extends RoomDatabase {

    //specifiy the Daos
    public abstract RunDao runDao();
    public abstract GPSDao gpsDao();

    private static volatile MainDatabase INSTANCE; //db instance
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //used to get a reference to the database
    public static MainDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MainDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MainDatabase.class, "runs_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(createCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    //initiliase database callback
    private static RoomDatabase.Callback createCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            Log.d("g53mdp", "dboncreate");
            databaseWriteExecutor.execute(() -> {
                    //insert dummy data
            });
        }
    };
}

