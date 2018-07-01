package com.alcwithgoogle.journalapp.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alcwithgoogle.journalapp.R;
import com.alcwithgoogle.journalapp.crud.DiaryDatabase;
import com.alcwithgoogle.journalapp.model.Diary;
import com.alcwithgoogle.journalapp.recycler.DiaryAdapter;
import com.alcwithgoogle.journalapp.utils.DiaryExecutors;
import com.alcwithgoogle.journalapp.utils.GridSpacingItemDecoration;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DiaryAdapter.ItemClickListener {
    // Log TAG
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recycler;
    private GridLayoutManager gridLayoutManager;
    private DiaryAdapter diaryAdapter;

    // boolean variable for double back click
    boolean doubleBackToExitPressedOnce = false;

    // diary database instance
    private DiaryDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind the recycler view to it layout
        recycler = findViewById(R.id.diaries_recycler);

        gridLayoutManager = new GridLayoutManager(this,2,
                LinearLayoutManager.VERTICAL,false);
        // the recyclerView has fixed size
        recycler.setHasFixedSize(true);
        // add the layout manager to the recycler
        recycler.setLayoutManager(gridLayoutManager);
        // add itemDecoration
        recycler.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10),
                true));

        // Initialize the adapter and attach it to the RecyclerView
        diaryAdapter = new DiaryAdapter(this,this);
        recycler.setAdapter(diaryAdapter);

        // diarydatabase instanciation
        mDatabase = DiaryDatabase.getDbInstance(getApplicationContext());

        // trigger the FAB action
        FloatingActionButton fab = findViewById(R.id.fab);
        // set onClick listener to our fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(NewDiary.get(MainActivity.this));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrievesDiaries();
    }

    private void retrievesDiaries() {
        DiaryExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Diary> diaryList = mDatabase.diaryDAO().loadALlDiaries();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        diaryAdapter.setDiaries(diaryList);
                    }
                });
            }
        });
    }

    // Utilities method to convert dp to px
    private int dpToPx(int dp) {
        Resources res = getResources();
        return Math.round(TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics()));
    }

    // Inflate the menu in Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        // trigger the action according to the user choice
        if (itemId == R.id.setting) {
            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        },2000);
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(MainActivity.this,NewDiary.class);
        intent.putExtra(NewDiary.EXTRA_DIARY_ID,itemId);
        startActivity(intent);
    }
}
