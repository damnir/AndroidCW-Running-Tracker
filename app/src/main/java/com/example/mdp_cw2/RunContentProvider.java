package com.example.mdp_cw2;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mdp_cw2.Database.MainDatabase;

//content provider for runs
public class RunContentProvider extends ContentProvider {
    private MainDatabase db;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(RunContract.AUTHORITY, "run", 1);
        uriMatcher.addURI(RunContract.AUTHORITY, "gps", 2);
    }


    @Override
    public boolean onCreate() {
        Log.d("g53mdp", "contentprovider oncreate");
        this.db = MainDatabase.getDatabase(this.getContext());

        return true;
    }

    /* case 1 - return all Run entreis
       case 2 - return all GPS coordinates
     */

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Log.d("g53mdp", uri.toString() + " " + uriMatcher.match(uri));

        final Cursor cursor;

        switch(uriMatcher.match(uri)) {
            case 1:
                cursor = db.runDao().cGetAllRuns();
                return cursor;
            case 2:
                cursor = db.gpsDao().cGetAllGPS();
                return cursor;

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String contentType;

        if (uri.getLastPathSegment()==null) {
            contentType = RunContract.CONTENT_TYPE_MULTIPLE;
        } else {
            contentType = RunContract.CONTENT_TYPE_SINGLE;
        }

        return contentType;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("not implemented");
    }
}
