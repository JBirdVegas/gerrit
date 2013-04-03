package com.aokp.gerrit.cards;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.aokp.gerrit.CardsActivity;
import com.aokp.gerrit.PatchSetViewerActivity;
import com.aokp.gerrit.R;
import com.aokp.gerrit.objects.JSONCommit;
import com.aokp.gerrit.tasks.GerritTask;
import com.fima.cardsui.objects.Card;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jbird
 * Date: 3/31/13
 * Time: 4:53 PM

 */
public class CommitCard extends Card {
    private JSONCommit mCommit;

    public CommitCard(JSONCommit commit) {
        this.mCommit = commit;
    }

    @Override
    public View getCardContent(final Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View commitCardView = inflater.inflate(R.layout.commit_card, null);
        ((TextView) commitCardView.findViewById(R.id.commit_card_commit_owner)).setText(mCommit.getOwner());
        ((TextView) commitCardView.findViewById(R.id.commit_card_project_name)).setText(mCommit.getProject());
        ((TextView) commitCardView.findViewById(R.id.commit_card_title)).setText(mCommit.getSubject());
        ((TextView) commitCardView.findViewById(R.id.commit_card_last_updated)).setText(mCommit.getLastUpdatedDate());
        ((TextView) commitCardView.findViewById(R.id.commit_card_commit_status)).setText(mCommit.getStatus().toString());
        TextView messageTv = (TextView) commitCardView.findViewById(R.id.commit_card_message);
        TextView changedFilesTv = (TextView) commitCardView.findViewById(R.id.commit_card_changed_files);
        Button browserView = (Button) commitCardView.findViewById(R.id.commit_card_view_in_browser);
        Button moarInfo = (Button) commitCardView.findViewById(R.id.commit_card_moar_info);
        // TODO FIX ME!!!
        moarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GerritTask task = new GerritTask() {
                    @Override
                    protected void onPostExecute(String returnedJSON) {
                        Intent patchsetViewer = new Intent(context, PatchSetViewerActivity.class);
                        patchsetViewer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        // pass all information about the current patchset from our direct query
                        patchsetViewer.putExtra(JSONCommit.KEY_PATCHSET_IN_JSON, returnedJSON);
                        // pass json original card was formed with
                        // TODO remove? we pass the json not the object
                        // allowing destination to handle object creation
                        patchsetViewer.putExtra(JSONCommit.KEY_JSON_COMMIT,
                                mCommit.getRawJSONCommit().toString());
                        // so our extras don't get recycled
                        patchsetViewer.setAction(JSONCommit.KEY_FOOBAR);
                        context.startActivity(patchsetViewer);
                    }
                };
                task.execute(CardsActivity.GERRIT + "changes/" + mCommit.getId() + "/details");
            }
        });
        browserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mCommit.getWebAddress()));
                context.startActivity(browserIntent);
            }
        });
        // we only have these if we direct query the commit specifically
        if (mCommit.getCurrentRevision() != null) {
            messageTv.setText(mCommit.getMessage());
            changedFilesTv.setText(buildChangedFilesString(mCommit.getChangedFiles()));
        } else {
            messageTv.setVisibility(View.GONE);
            changedFilesTv.setVisibility(View.GONE);
        }
        return commitCardView;
    }

    public void update(JSONCommit commit) {
        this.mCommit = commit;
    }

    /**
     * TODO!!!
     * @param fileList
     * @return
     */
    private String buildChangedFilesString(List<JSONCommit.ChangedFile> fileList) {
        return "gibber\njabbing\nabout gibber\njabbing";
    }

    public int getNumber() {
        return mCommit.getCommitNumber();
    }

    public JSONCommit getJSONCommit() {
        return this.mCommit;
    }
}