package ar.edu.unc.famaf.redditreader.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.backend.Backend;
import ar.edu.unc.famaf.redditreader.backend.GetTopPostsTask;
import ar.edu.unc.famaf.redditreader.backend.ReeditDBHelper;
import ar.edu.unc.famaf.redditreader.backend.TopPostIterator;
import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsActivityFragment extends Fragment implements TopPostIterator {
    PostAdapter adapter;
    public NewsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View lvView =  inflater.inflate(R.layout.fragment_news, container, false);
        ArrayList<PostModel> listPost = new ArrayList<PostModel>();

        Backend.getInstance().getTopPosts(getContext(), listPost);


        adapter = new PostAdapter(getContext(), R.layout.row_layout, listPost);
        ListView listView = (ListView) lvView.findViewById(R.id.list_view_id);
        listView.setAdapter(adapter);

        return lvView;
    }

    public void notifyNewsRetrieved() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void nextPosts(List<PostModel> listPost) {
        adapter.notifyDataSetChanged();
    }
}
