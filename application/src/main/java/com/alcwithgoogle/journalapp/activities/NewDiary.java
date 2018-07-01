package com.alcwithgoogle.journalapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.alcwithgoogle.journalapp.R;
import com.alcwithgoogle.journalapp.crud.DiaryDatabase;
import com.alcwithgoogle.journalapp.model.Diary;
import com.alcwithgoogle.journalapp.recycler.DiaryAdapter;
import com.alcwithgoogle.journalapp.utils.DiaryExecutors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewDiary extends AppCompatActivity {

    // Log TAG
    private static final String TAG = NewDiary.class.getSimpleName();

    // Extra for the diary ID to be received in the intent
    public static final String EXTRA_DIARY_ID = "extraDiaryId";

    // Constant for default diary id to be used when not in update mode
    private static final int DEFAULT_DIARY_ID = -1;

    // Extra for the diary ID to be received after rotation
    public static final String INSTANCE_DIARY_ID = "instanceDiaryId";

    private int mDiaryId = DEFAULT_DIARY_ID;

    // Editext variables
    private EditText editTextTitle, editTextDescription;

    // database instance variable
    private DiaryDatabase mDatabase;

    // static method to return a NewDiary intent
    public static Intent get(Context context) {
        return new Intent(context,NewDiary.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diary);

        initViews();

        // get the database instance
        mDatabase = DiaryDatabase.getDbInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_DIARY_ID)) {
            mDiaryId = savedInstanceState.getInt(INSTANCE_DIARY_ID, DEFAULT_DIARY_ID);
        }

        // for update
        Intent intent = getIntent();

        if (mDiaryId == DEFAULT_DIARY_ID) {
            mDiaryId = intent.getIntExtra(EXTRA_DIARY_ID, DEFAULT_DIARY_ID);

            DiaryExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    final Diary diary = mDatabase.diaryDAO().loadById(mDiaryId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            populateUI(diary);
                        }
                    });
                }
            });
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_DIARY_ID,mDiaryId);
        super.onSaveInstanceState(outState);
    }

    //initViews is called from onCreate to init the member variable views
    private void initViews() {
        // Initalize members variables
        editTextTitle = findViewById(R.id.new_diary_title);
        editTextDescription = findViewById(R.id.new_diary_description);
    }

    // populateUI would be called to populate the UI when in update mode
    private void populateUI(Diary diary) {

        // return null if diary is null
        if (diary == null) {
            return;
        }

        // populate the ui using the variables
        editTextTitle.setText(diary.getTitle());
        editTextDescription.setText(diary.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        // trigger the action according to the user choice
        if (itemId == R.id.action_accept) {
            save();
            finish();
            return true;
        } else if (itemId == R.id.action_delete) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        String diaryTitle = editTextTitle.getText().toString();
        String diaryDescription = editTextDescription.getText().toString();
        Date date = new Date();

        // diary object to push in the database
        final Diary diary = new Diary(diaryTitle,diaryDescription,date);

        // check if the entered text is valid
        if (TextUtils.isEmpty(diaryTitle) || TextUtils.isEmpty(diaryDescription)) {
            Toast.makeText(this,"You must enter at least a title",Toast.LENGTH_SHORT).show();
        } else {
            DiaryExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    // insert the diary only if mDiaryId matches DEFAULT_DIARY_ID
                    // otherwhise, update it
                    if (mDiaryId == DEFAULT_DIARY_ID) {
                        // insert
                        mDatabase.diaryDAO().insert(diary);
                    } else {
                        // update
                        diary.setId(mDiaryId);
                        mDatabase.diaryDAO().update(diary);
                    }
                    finish();
                }
            });
        }
    }
}