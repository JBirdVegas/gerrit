package com.jbirdvegas.mgerrit;

/*
 * Copyright (C) 2013 Android Open Kang Project (AOKP)
 *  Author: Jon Stanford (JBirdVegas), 2013
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Prefs extends PreferenceActivity {
    private static final String PREV_GERRIT = "previous_gerrit";
    ListPreference mGerritList;
    private static final String GERRIT_KEY = "gerrit_instances_key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        mGerritList = (ListPreference) findPreference(GERRIT_KEY);
        mGerritList.setSummary(mGerritList.getValue());
        mGerritList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                preference.setSummary((CharSequence) o);
                StaticWebAddress.setGERRIT_INSTANCE_WEBSITE((String) o);
                Toast.makeText(getApplicationContext(), "Using Gerrit: " + o, Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    public static String getCurrentGerrit(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(GERRIT_KEY, StaticWebAddress.HTTP_GERRIT_SUDOSERVERS_COM);
    }

    public static String getSavedGerrit(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREV_GERRIT, StaticWebAddress.HTTP_GERRIT_SUDOSERVERS_COM);
    }

    public static void setSavedGerrit(Context context, String saveString) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREV_GERRIT, saveString)
                .commit();
    }
}
