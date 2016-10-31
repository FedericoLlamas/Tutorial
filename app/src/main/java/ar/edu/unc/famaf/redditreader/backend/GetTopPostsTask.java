package ar.edu.unc.famaf.redditreader.backend;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;
import ar.edu.unc.famaf.redditreader.ui.NewsActivity;
import ar.edu.unc.famaf.redditreader.ui.NewsActivityFragment;

/**
 * Created by federico on 29/10/16.
 */

public class GetTopPostsTask extends AsyncTask {

    List<PostModel> postList;
    NewsActivityFragment frgActivity;

    public GetTopPostsTask(List<PostModel> postList, NewsActivityFragment listener) {
        this.postList = postList;
        this.frgActivity = listener;
    }

    @Override
    protected List<PostModel> doInBackground(Object... params) {
        List<PostModel> listTop50 = null;
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        //String forecastJsonStr = null;
        try {
            conn = (HttpURLConnection) new URL("https://www.reddit.com/r/AskReddit/.json?limit=2").openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int resCode = conn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                listTop50 = new Parser().readJsonStream(inputStream);
            }

            Iterator<PostModel> it = listTop50.iterator();
            while (it.hasNext()) {
                postList.add(it.next());
            }
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
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
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        frgActivity.notifyNewsRetrieved();
    }
}
