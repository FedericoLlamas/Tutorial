package ar.edu.unc.famaf.redditreader.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import ar.edu.unc.famaf.redditreader.backend.DBAdapter;
import ar.edu.unc.famaf.redditreader.model.PostModel;

public class VoteAction {
    PostModelHolder holder;
    PostModel model;
    int clicks;
    DBAdapter db;

    private Context context;
    int score;

    public VoteAction(PostModel model, PostModelHolder holder, DBAdapter db, Context context, int clicks, int score) {
        this.model = model;
        this.holder = holder;
        this.db = db;
        this.context = context;
        this.clicks = clicks;
        this.score = score;
    }

    public PostModel ButtonBehavior(final String dir) {

        if (NewsActivity.LOGIN) {
            if (dir.equals("1")) {
                clicks = model.getClickup() + 1;
            } else {
                clicks = model.getClickdown() + 1;
            }
            if (clicks == 2) {
                new VoteTask(model.getName()) {
                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        if (aBoolean) {
                            model.setScore(score);
                            holder.score.setText(String.valueOf(model.getScore()));
                            db.updateData(model, "SCORE");
                            if (dir.equals("1")) {
                                holder.up.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                                model.setClickup(0);
                                holder.down.setVisibility(View.VISIBLE);

                            } else {
                                holder.down.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                                model.setClickdown(0);
                                holder.up.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (!NewsActivity.ACTIVE_USER) {
                                Toast.makeText(context, "Unauthorized. Logout!", Toast.LENGTH_LONG).show();
                                ((Activity) context).finish();
                            }
                        }
                    }
                }.execute("0");


            } else {
                new VoteTask(model.getName()) {
                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        System.out.println(aBoolean);
                        if (aBoolean) {
                            if (dir.equals("1")) {
                                model.setClickdown(0);
                                model.setClickup(1);
                                holder.up.setColorFilter(Color.parseColor("#ff8b60"), PorterDuff.Mode.SRC_ATOP);
                                holder.down.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                                model.setScore(score + 1);
                                holder.down.setVisibility(View.INVISIBLE);
                            } else {
                                model.setClickup(0);
                                model.setClickdown(1);
                                holder.down.setColorFilter(Color.parseColor("#ff8b60"), PorterDuff.Mode.SRC_ATOP);
                                holder.up.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                                model.setScore(score - 1);
                                holder.up.setVisibility(View.INVISIBLE);
                            }
                            holder.score.setText(String.valueOf(model.getScore()));
                            db.updateData(model, "SCORE");
                        } else {
                            if (!NewsActivity.ACTIVE_USER) {
                                Toast.makeText(context, "Unauthorized. Logout!", Toast.LENGTH_LONG).show();
                                ((Activity) context).finish();
                            }
                        }
                    }
                }.execute(dir);
            }

        } else {
            String url = String.format(NewsActivity.AUTH_URL, NewsActivity.CLIENT_ID, NewsActivity.STATE, NewsActivity.REDIRECT_URI);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        }
        return model;
    }
}

