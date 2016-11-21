package ar.edu.unc.famaf.redditreader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.backend.OnPostItemSelectedListener;
import ar.edu.unc.famaf.redditreader.model.PostModel;


public class NewsActivity extends AppCompatActivity implements OnPostItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sign_in) {
            TextView textView = (TextView) findViewById(R.id.loginStatusTextView);
            textView.setText("User XXXX logged in");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPostItemPicked(PostModel postModel) {
        Intent intent = new Intent(this, NewsDetailActivity.class);
        if (postModel != null){
            intent.putExtra("icon",postModel.getIcon());
            intent.putExtra("title",postModel.getTitle());
            intent.putExtra("author",postModel.getAuthor());
            intent.putExtra("subreddit",postModel.getSubreddit());
            intent.putExtra("date",String.valueOf(postModel.getCreated()));
            intent.putExtra("url",postModel.getUrl());
            intent.putExtra("comment",postModel.getComments());
            startActivityForResult(intent, 0);
        }

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }
}
