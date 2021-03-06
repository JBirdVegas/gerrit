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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.fima.cardsui.views.CardUI;
import com.jbirdvegas.mgerrit.cards.PatchSetChangesCard;
import com.jbirdvegas.mgerrit.cards.PatchSetMessageCard;
import com.jbirdvegas.mgerrit.cards.PatchSetPropertiesCard;
import com.jbirdvegas.mgerrit.cards.PatchSetReviewersCard;
import com.jbirdvegas.mgerrit.objects.JSONCommit;
import com.jbirdvegas.mgerrit.tasks.GerritTask;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * Class handles populating the screen with several
 * cards each giving more information about the patchset
 *
 * All cards are located at jbirdvegas.mgerrit.cards.*
 */
public abstract class PatchSetViewerActivity extends Activity {
    private static final String TAG = PatchSetViewerActivity.class.getSimpleName();
    private CardUI mCardsUI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commit_list);
        String query = getIntent().getStringExtra(JSONCommit.KEY_WEBSITE);
        Log.d(TAG,"Website to query: " + query);
        mCardsUI = (CardUI) findViewById(R.id.commit_cards);
        executeGerritTask(query);
    }

    private void executeGerritTask(final String query) {
        new GerritTask() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    addCards(mCardsUI,
                            new JSONCommit(
                                    new JSONArray(s).getJSONObject(0),
                                    getApplicationContext()));
                } catch (JSONException e) {
                    Log.d(TAG, "Response from "
                            + query + " could not be parsed into cards :(", e);
                }
            }
        }.execute(query);
    }

    private void addCards(CardUI ui, JSONCommit jsonCommit) {
        Log.d(TAG, "Loading Properties Card...");
        ui.addCard(new PatchSetPropertiesCard(jsonCommit));
        Log.d(TAG, "Loading Message Card...");
        ui.addCard(new PatchSetMessageCard(jsonCommit));
        Log.d(TAG, "Loading Changes Card...");
        ui.addCard(new PatchSetChangesCard(jsonCommit));
        Log.d(TAG, "Loading Reviewers Card...");
        ui.addCard(new PatchSetReviewersCard(jsonCommit));
        ui.refresh();
    }

    /*
    Possible cards

    --Patch Set--
    Select patchset number to display in these cards
    -------------

    --Times Card--
    Original upload time
    Most recent update
    --------------

    --Inline comments Card?--
    Show all comments inlined on code view pages
    **may be kind of pointless without context of surrounding code**
    * maybe a webview for each if possible? *
    -------------------------

     */

    // Handles correctly setting the ListViews height based on all the children
    // from http://nex-otaku-en.blogspot.com/2010/12/android-put-listview-in-scrollview.html
    public static void setListViewHeightBasedOnChildren(ListView... listViews) {
        for (ListView listView : listViews) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = getTotalHeight(listView, listAdapter)
                    + listView.getDividerHeight() * (listAdapter.getCount() - 1);
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }

    static int getTotalHeight(ListView listView, ListAdapter listAdapter) {
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(
                listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        return totalHeight;
    }


    public static void setNotFoundListView(Context context, ListView listView) {
        listView.setAdapter(
                new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[]{
                                context.getString(R.string.none_found),
                                context.getString(R.string.please_try_again)
                        }));
    }
}