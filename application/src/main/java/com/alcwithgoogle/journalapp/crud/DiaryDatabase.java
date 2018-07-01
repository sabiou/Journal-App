package com.alcwithgoogle.journalapp.crud;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.alcwithgoogle.journalapp.model.Diary;
import com.alcwithgoogle.journalapp.utils.DateConverter;

@Database(entities = {Diary.class}, version = 1,exportSchema = false)
@TypeConverters(DateConverter.class)

public abstract class DiaryDatabase extends RoomDatabase {
    // LOG TAG
    private static final String LOG_TAG = DiaryDatabase.class.getSimpleName();

    // LOCK
    private static final Object OBJECT_LOCK = new Object();

    // database name
    private static final String DATABASE_NAME = "diary";

    // db instance
    private static DiaryDatabase dbInstance;

    public static DiaryDatabase getDbInstance(Context context) {

        if (dbInstance == null) {
            synchronized (OBJECT_LOCK) {
                Log.d(LOG_TAG,"Creating a database instance");
                dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                        DiaryDatabase.class,DiaryDatabase.DATABASE_NAME)
                        .build();
            }
        } Log.d(LOG_TAG,"Getting database instance");

        return dbInstance;
    }

    // abstract method to perform dao request
    public abstract DiaryDAO diaryDAO();
}
