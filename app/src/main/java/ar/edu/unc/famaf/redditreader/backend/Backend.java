package ar.edu.unc.famaf.redditreader.backend;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;

public class Backend {

    private static Backend ourInstance = new Backend();
    public static Backend getInstance() {
        return ourInstance;
    }

    private Backend() {}

    public void getTopPosts(Context context, TopPostIterator listener){

        List<PostModel> listPost = new ArrayList<PostModel>();
        ReeditDBHelper readDbHelper = new ReeditDBHelper(context);

        if (isOnline(context)){
            new GetTopPostsTask(context, listener).execute();
        }
        else{
            SQLiteDatabase db = readDbHelper.getReadableDatabase();
            if(db != null)
            {
                Cursor cursor = db.rawQuery("select * from post_table", null);

                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        String title = cursor.getString(cursor.getColumnIndex(Parser.TITLE_KEY));
                        String author = cursor.getString(cursor.getColumnIndex(Parser.AUTHOR_KEY));
                        String date = cursor.getString(cursor.getColumnIndex(Parser.DATA_KEY));
                        String comments = cursor.getString(cursor.getColumnIndex(Parser.COMMENTS_KEY));
                        String url = cursor.getString(cursor.getColumnIndex(Parser.THUMBNAIL_KEY));
                        listPost.add(new PostModel(title, author, date, comments, url));
                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
            listener.onPostGot(listPost);
        }
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
