package ar.edu.unc.famaf.redditreader.backend;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by federico on 29/10/16.
 */

public class Parser {

    public List<PostModel> readJsonStream(InputStream in) throws IOException, JSONException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        ArrayList<PostModel> list = new ArrayList<PostModel>();
        try {
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
            }
            return list;

        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
