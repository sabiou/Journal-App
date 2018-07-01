package com.alcwithgoogle.journalapp.crud;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.alcwithgoogle.journalapp.model.Diary;

import java.util.List;

// interface that handle all our CRUD method
@Dao
public interface DiaryDAO {

    // Query method to  select all diaries
    @Query("SELECT * FROM diary")
    List<Diary> loadALlDiaries();

    // insert diary method
    @Insert
    void insert(Diary diary);

    // update diary method
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Diary diary);

    @Delete
    void delete(Diary diary);

    // Query method to load a diary by id
    @Query("SELECT * FROM diary where id = :id")
    Diary loadById(int id);

}
