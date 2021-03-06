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

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

public class GerritControllerActivity extends TabActivity {
    private static final String TAG = GerritControllerActivity.class.getSimpleName();
    private TabHost mTabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Setup tabs //
        mTabHost = getTabHost();
        addTabs();
    }

    private void addTabs() {
        // Review tab
        Intent intentReview = new Intent()
                .setClass(this, ReviewTab.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TabHost.TabSpec tabSpecReview = mTabHost
                .newTabSpec(getString(R.string.reviewable))
                .setContent(intentReview)
                .setIndicator(getString(R.string.reviewable));
        mTabHost.addTab(tabSpecReview);

        // Merged tab
        Intent intentMerged = new Intent()
                .setClass(this, MergedTab.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TabHost.TabSpec tabSpecMerged = mTabHost
                .newTabSpec(getString(R.string.merged))
                .setContent(intentMerged)
                .setIndicator(getString(R.string.merged));
        mTabHost.addTab(tabSpecMerged);

        // Abandon tab
        Intent intentAbandon = new Intent()
                .setClass(this, AbandonedTab.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TabHost.TabSpec tabSpecAbandon = mTabHost
                .newTabSpec(getString(R.string.abandoned))
                .setContent(intentAbandon)
                .setIndicator(getString(R.string.abandoned));
        mTabHost.addTab(tabSpecAbandon);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gerrit_instances_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, Prefs.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Prefs.setSavedGerrit(getApplicationContext(), Prefs.getCurrentGerrit(getApplicationContext()));
        startActivity(intent);
        return true;
    }
}
