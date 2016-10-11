package ar.edu.unc.famaf.redditreader.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;
import ar.edu.unc.famaf.redditreader.backend.Backend;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsActivityFragment extends Fragment {

    public NewsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View lv_view =  inflater.inflate(R.layout.fragment_news, container, false);

        List<PostModel> postLst = Backend.getInstance().getTopPosts();
        PostAdapter adapter = new PostAdapter(getContext(), R.layout.row_layout, postLst);
        ListView list_view = (ListView) lv_view.findViewById(R.id.list_view_id);
        list_view.setAdapter(adapter);

        return lv_view;

    }
}
