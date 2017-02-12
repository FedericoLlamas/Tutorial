package ar.edu.unc.famaf.redditreader.ui;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
public class PostModelHolder {
    TextView title;
    TextView subreddit;
    TextView created;
    TextView author;
    ImageView icon;
    TextView comments;
    TextView score;

    ProgressBar progressBar;
    ImageButton up;
    ImageButton down;
    int position;
}
