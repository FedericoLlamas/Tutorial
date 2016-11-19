package ar.edu.unc.famaf.redditreader.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.backend.Backend;
import ar.edu.unc.famaf.redditreader.backend.EndlessScrollListener;
import ar.edu.unc.famaf.redditreader.backend.GetTopPostsTask;
import ar.edu.unc.famaf.redditreader.backend.Parser;
import ar.edu.unc.famaf.redditreader.backend.ReeditDBHelper;
import ar.edu.unc.famaf.redditreader.backend.TopPostIterator;
import ar.edu.unc.famaf.redditreader.model.Listing;
import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsActivityFragment extends Fragment{

    public NewsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View lvView =  inflater.inflate(R.layout.fragment_news, container, false);
        final List<PostModel> listPost= new ArrayList<>();
        final boolean[] mBusy = new boolean[1];

        Backend.getInstance().getTopPosts(0, getContext(), new TopPostIterator() {
            @Override
            public void nextPosts(List<PostModel> list, ReeditDBHelper db) {
                listPost.addAll(list);
                final PostAdapter adapter = new PostAdapter(getActivity(), R.layout.row_layout, listPost, db, mBusy[0]);
                final ListView lv = (ListView) getView().findViewById(R.id.list_view_id);
                lv.setAdapter(adapter);

                lv.setOnScrollListener(new EndlessScrollListener() {
                    private int currentScrollState;
                    @Override
                    public boolean onLoadMore(int page, int totalItemsCount) {
                        System.out.println("Load 5 more");
                        System.out.println("Total loaded: "+totalItemsCount);
                        Backend.getInstance().getTopPosts(totalItemsCount, getContext(), new TopPostIterator() {
                            @Override
                            public void nextPosts(List<PostModel> list, ReeditDBHelper db) {
                                listPost.addAll(list);
                                adapter.notifyDataSetChanged();
                            }
                        });
                        return true;
                    }

                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        super.onScrollStateChanged(view, scrollState);

                        switch (scrollState) {
                            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                                mBusy[0] = false;
                                adapter.notifyDataSetChanged();
                                break;
                            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                                mBusy[0] = true;
                                break;
                            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                                mBusy[0] = true;
                                break;
                        }
                    }
                });


            }
        });
        return lvView;
    }
}
