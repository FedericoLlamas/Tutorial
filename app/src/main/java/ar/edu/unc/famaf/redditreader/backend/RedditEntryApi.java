package ar.edu.unc.famaf.redditreader.backend;

import android.provider.BaseColumns;

/**
 * Created by federico on 18/11/16.
 */

public class RedditEntryApi {
    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME="postModel";
        public static final String ID="id";
        public static final String AUTHOR="mAuthor";
        public static final String TITLE="mTitle";
        public static final String SUBREDDIT="mSubreddit";
        public static final String COMMENTS="comments";
        public static final String CREATED="mCreated";
        public static final String URL="url";
        public static  final String ICON = "icon";
        /*agregar en postmodel id*/
    }
}
