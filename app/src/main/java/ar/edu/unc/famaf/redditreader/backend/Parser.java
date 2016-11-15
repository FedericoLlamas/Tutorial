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

    public static final String DATA_KEY = "data";
    public static final String CHILDREN_KEY = "children";
    public static final String CREATED_KEY = "created";
    public static final String TITLE_KEY = "title";
    public static final String AUTHOR_KEY = "author";
    public static final String COMMENTS_KEY = "num_comments";
    public static final String THUMBNAIL_KEY = "thumbnail";

    public List<PostModel> readJsonStream(InputStream in) throws IOException, JSONException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        ArrayList<PostModel> list = new ArrayList<PostModel>();
        try {
            JSONObject response = new JSONObject(String.valueOf(reader.readLine()));
            JSONObject data = response.getJSONObject(DATA_KEY);
            JSONArray hotTopics = data.getJSONArray(CHILDREN_KEY);
            for (int i = 0; i < hotTopics.length(); i++) {
                JSONObject topic = hotTopics.getJSONObject(i).getJSONObject(DATA_KEY);

                String time = new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(Double.valueOf(topic.getLong(CREATED_KEY)).longValue()*1000));
                String title = topic.getString(TITLE_KEY);
                String author = topic.getString(AUTHOR_KEY);
                String postTime = time.toString();
                String comment = topic.getString(COMMENTS_KEY);
                String imageUrl = topic.getString(THUMBNAIL_KEY);

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
