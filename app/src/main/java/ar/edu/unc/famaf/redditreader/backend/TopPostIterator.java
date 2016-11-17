package ar.edu.unc.famaf.redditreader.backend;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by federico on 03/11/16.
 */

public interface TopPostIterator {
    void onPostGot(List<PostModel> posts);
}
