package com.alcwithgoogle.journalapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "diary")
public class Diary {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;

    @ColumnInfo(name = "updated_at")
    private Date date;

    public Diary() {}

    @Ignore // tell Room to ignore this constructor at runtime
    public Diary(String title, String description, Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public Diary(int id, String title, String description, Date date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
