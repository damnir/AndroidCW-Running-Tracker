package com.example.mdp_cw2;

import android.net.Uri;

//contract for the content provider
public class RunContract {

    public static final String AUTHORITY = "com.example.mdp_cw2";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri RUN_URI = Uri.parse("content://" + AUTHORITY + "/run/");
    public static final Uri GPS_URI = Uri.parse("content://" + AUTHORITY + "/gps/");

    public static final String RUN_ID = "_id";
    public static final String RUN_DATE = "date";
    public static final String RUN_TIME = "time";
    public static final String RUN_SATISFACTION = "satisfaction";
    public static final String RUN_DISTANCE = "distance";
    public static final String RUN_SPEED = "speed";
    public static final String RUN_PACE = "pace";
    public static final String RUN_NOTE = "note";
    public static final String RUN_FAVOURITE = "favourite";

    public static final String GPS_ID = "_id";
    public static final String GPS_RUN_ID = "run_id";
    public static final String GPS_TIME = "time";
    public static final String GPS_LAT = "latitude";
    public static final String GPS_LNG = "longitude";


    public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/RecipeProvider.data.text";
    public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/RecipeProvider.data.text";

}