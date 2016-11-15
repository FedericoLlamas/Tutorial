package ar.edu.unc.famaf.redditreader.backend;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;


/**
 * Created by federico on 29/10/16.
 */

public class GetTopPostsTask extends AsyncTask <Void, Integer, List<PostModel>>{
    private static final String URL_TO_REDDIIT_API = "https://www.reddit.com/r/todayilearned/top.json?limit=50";

    public GetTopPostsTask() {
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

            conn = (HttpURLConnection) new URL(URL_TO_REDDIIT_API).openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int resCode = conn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                listTop50 = new Parser().readJsonStream(inputStream);
            }

            Iterator<PostModel> it = listTop50.iterator();
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
    }
}
