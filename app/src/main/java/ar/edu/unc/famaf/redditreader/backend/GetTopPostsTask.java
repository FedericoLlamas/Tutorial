package ar.edu.unc.famaf.redditreader.backend;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;
import ar.edu.unc.famaf.redditreader.ui.NewsActivityFragment;
import ar.edu.unc.famaf.redditreader.ui.PostAdapter;

/**
 * Created by federico on 29/10/16.
 */

public class GetTopPostsTask extends AsyncTask<Object, Object, List<PostModel>> {

    @Override
    protected Object doInBackground(Object... params) {
        //ArrayList<PostModel> listTop50 = new ArrayList<PostModel>();
        List<PostModel> listTop50 = null;
        HttpURLConnection conn = null;
        //conn.getInputStream();
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

            /*reader = new BufferedReader(new InputStreamReader(inputStream));
            //return forecastJsonStr;
            JSONObject response = new JSONObject(String.valueOf(reader.readLine()));
            JSONObject data = response.getJSONObject("data");
            JSONArray hotTopics = data.getJSONArray("children");
            for (int i = 0; i < hotTopics.length(); i++) {
                JSONObject topic = hotTopics.getJSONObject(i).getJSONObject("data");

                String title = topic.getString("title");
                String author = topic.getString("author");
                String postTime = topic.getString("created_utc");
                String comment = "10";
                String imageUrl = topic.getString("thumbnail");
                list.add(new PostModel(title, author, postTime, comment, R.drawable.icono, imageUrl));
                String hola = "pepe";
            }*/

            return listTop50;

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

    /*@Override
    protected void onPostExecute(List<PostModel> list) {
        super.onPostExecute(list);
        returnTopPost(list);
    }

    protected List<PostModel> returnTopPost(List<PostModel> list1){
        return list1;

    }*/
}
