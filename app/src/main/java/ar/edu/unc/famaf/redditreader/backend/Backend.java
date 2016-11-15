package ar.edu.unc.famaf.redditreader.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

    public List<PostModel> getTopPosts(Context context, List<PostModel> listPost){

        final TopPostIterator listener = null;

        ReeditDBHelper readDbHelper = new ReeditDBHelper(context);

        if (isOnline(context)){
            final SQLiteDatabase db = readDbHelper.getWritableDatabase();
            if(db != null)
            {
                readDbHelper.removeData();

                new GetTopPostsTask(){
                    protected void onPostExecute(List<PostModel> response){
                        insertTable(response, db);
                    }
                }.execute();

            }
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
            else{
                listPost = null;
            }
        }
        return listPost;
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void insertTable(List<PostModel> listPost, SQLiteDatabase db){
        for(int i=0; i <= listPost.size()-1; i++)
        {
            String title = listPost.get(i).getTitle();
            String author = listPost.get(i).getAuthor();
            String date = listPost.get(i).getDate();
            String comments = listPost.get(i).getComments();
            String url = listPost.get(i).getUrl();

            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("author", author);
            values.put("date", date);
            values.put("comments", comments);
            values.put("url", url);

            long newRowId = db.insert("post_table", null, values);
        }
    }
}
