package com.alcwithgoogle.journalapp.activities;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.alcwithgoogle.journalapp.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.prefs);
    }
}
