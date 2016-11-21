package ar.edu.unc.famaf.redditreader.backend;

import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by federico on 20/11/16.
 */

public interface OnPostItemSelectedListener {
    void onPostItemPicked(PostModel post);
}
