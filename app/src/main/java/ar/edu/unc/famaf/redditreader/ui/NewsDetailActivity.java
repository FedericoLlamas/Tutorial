package ar.edu.unc.famaf.redditreader.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ar.edu.unc.famaf.redditreader.R;

/**
 * Created by federico on 19/11/16.
 */

public class NewsDetailActivity extends AppCompatActivity implements NewsDetailActivityFragment.OnFragmentInteractionListener{

    public NewsDetailActivity() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            NewsDetailActivityFragment fragmentDetail = new NewsDetailActivityFragment();

            Intent intent = getIntent();
            byte[] icon = intent.getByteArrayExtra("icon");
            String title = intent.getStringExtra("title");
            String author = intent.getStringExtra("author");
            String subreddit = intent.getStringExtra("subreddit");
            String date = intent.getStringExtra("date");
            String url = intent.getStringExtra("url");
            int comment = intent.getIntExtra("comment", 0);

            NewsDetailActivityFragment fragment =
                    NewsDetailActivityFragment.newInstance(icon, title, author, subreddit, date, url, comment);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_detail, fragment).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Context context= getApplicationContext();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri, context, WebViewActivity.class);
        startActivityForResult(intent, 0);

    }

}
