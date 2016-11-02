package ar.edu.unc.famaf.redditreader.backend;

import android.app.ProgressDialog;
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
import ar.edu.unc.famaf.redditreader.ui.NewsActivityFragment;


/**
 * Created by federico on 29/10/16.
 */

public class GetTopPostsTask extends AsyncTask {

    private List<PostModel> postList;
    private NewsActivityFragment frgActivity;
    private ProgressDialog dialog;

    public GetTopPostsTask(List<PostModel> postList, NewsActivityFragment listener) {
        this.postList = postList;
        this.frgActivity = listener;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(this.frgActivity.getContext());
        dialog.setMessage("Loading...");
        dialog.show();
    }

    @Override
    protected List<PostModel> doInBackground(Object... params) {
        List<PostModel> listTop50 = null;
        HttpURLConnection conn = null;
        try {

            conn = (HttpURLConnection) new URL("https://www.reddit.com/r/todayilearned/top.json?limit=50").openConnection();
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
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        frgActivity.notifyNewsRetrieved();
    }
}
