package ar.edu.unc.famaf.redditreader.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;


/**
 * Created by federico on 29/10/16.
 */

public class GetTopPostsTask extends AsyncTask <Void, Integer, List<PostModel>>{
    private static final String URL_TO_REDDIT_API = "https://www.reddit.com/r/todayilearned/top.json?limit=50";
    private TopPostIterator listener;
    private Context context;

    public GetTopPostsTask(Context context, TopPostIterator listener) {
        this.listener = listener;
        this.context = context;

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<PostModel> doInBackground(Void... params) {
        List<PostModel> listTop50 = null;
        HttpURLConnection conn = null;
        try {

            conn = (HttpURLConnection) new URL(URL_TO_REDDIT_API).openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int resCode = conn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                listTop50 = new Parser().readJsonStream(inputStream);
            }
            ReeditDBHelper readDbHelper = new ReeditDBHelper(context);
            SQLiteDatabase db = readDbHelper.getWritableDatabase();
            if(db != null)
            {
                readDbHelper.removeData();
                for(int i=0; i <= listTop50.size()-1; i++)
                {
                    String title = listTop50.get(i).getTitle();
                    String author = listTop50.get(i).getAuthor();
                    String date = listTop50.get(i).getDate();
                    String comments = listTop50.get(i).getComments();
                    String url = listTop50.get(i).getUrl();

                    ContentValues values = new ContentValues();
                    values.put("title", title);
                    values.put("author", author);
                    values.put("date", date);
                    values.put("comments", comments);
                    values.put("url", url);

                    long newRowId = db.insert("post_table", null, values);
                }
            }
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if (conn != null) {
                conn.disconnect();
            }
        }
        return listTop50;
    }

    @Override
    protected void onPostExecute(List<PostModel> listTop50) {
        super.onPostExecute(listTop50);
        listener.onPostGot(listTop50);
    }
}
