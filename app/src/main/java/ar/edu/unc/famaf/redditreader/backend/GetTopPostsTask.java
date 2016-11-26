package ar.edu.unc.famaf.redditreader.backend;


import android.os.AsyncTask;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import ar.edu.unc.famaf.redditreader.model.Listing;

/**
 * Created by dvr on 20/10/16.
 */

public class GetTopPostsTask extends AsyncTask<String, Integer, Listing> {
    private String after;
    private int offset;

    public GetTopPostsTask(String after, int offset){
        this.after= after;
        this.offset=offset;
    }

    @Override
    protected Listing doInBackground(String... params) {
        String url;
        if(after!=null){
            url= params[0]+"&count="+ String.valueOf(offset)+"&after="+after;
        }else {
            url = params[0];
        }
        HttpURLConnection connection;
        try {
            connection=(HttpURLConnection)new URL(url).openConnection();
            connection.setReadTimeout(30000);
            connection.setRequestMethod("GET");
            Parser list = new Parser();

            return list.readJsonStream(connection.getInputStream());

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    @Override
    protected void onPostExecute(Listing input) {
        super.onPostExecute(input);
    }



}
