package ar.edu.unc.famaf.redditreader.backend;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

                String dateString = new SimpleDateFormat("dd/MM/yyyy").format(new Date(topic.getLong("created_utc")));

                String title = topic.getString("title");
                String author = topic.getString("author");
                String postTime = dateString;
                String comment = topic.getString("num_comments");
                //String imageUrl = "http://cdn.revistagq.com/uploads/images/thumbs/201525/reddit_5253_645x485.png";
                String imageUrl = topic.getString("thumbnail");
                list.add(new PostModel(title, author, postTime, comment, R.drawable.icono, imageUrl));
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
