package ar.edu.unc.famaf.redditreader.backend;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;

import static ar.edu.unc.famaf.redditreader.R.string.date;

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

                String time = new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(Double.valueOf(topic.getLong("created")).longValue()*1000));
                String title = topic.getString("title");
                String author = topic.getString("author");
                String postTime = time.toString();
                String comment = topic.getString("num_comments");
                String imageUrl = topic.getString("thumbnail");
                list.add(new PostModel(title, author, postTime, comment, imageUrl));
            }
            return list;

        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
