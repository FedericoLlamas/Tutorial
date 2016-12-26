package ar.edu.unc.famaf.redditreader.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.backend.ReeditDBHelper;

import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by dvr on 07/10/16.
 */

public class PostAdapter extends ArrayAdapter {
    private Context context;
    private int rsId;
    private List<PostModel> ListPostModel;
    private ReeditDBHelper db;
    private boolean mbusy;

    public PostAdapter(Context context, int resource, List<PostModel> list, ReeditDBHelper db, boolean mBusy) {
        super(context, resource, list);
        ListPostModel = list;
        this.context = context;
        this.rsId = resource;
        this.db = db;
        this.mbusy=mBusy;

    }

    @Override
    public int getCount() {
        return ListPostModel.size();
    }

    @Nullable
    @Override
    public PostModel getItem(int position) {
        return ListPostModel.get(position);
    }

    public int getPosition(PostModel item) {
        return ListPostModel.indexOf(item);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        final PostModelHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(rsId, parent, false);

            holder = new PostModelHolder();
            holder.author = (TextView) row.findViewById(R.id.author_id);
            holder.created = (TextView) row.findViewById(R.id.date_id);
            holder.subReddit = (TextView) row.findViewById(R.id.sub_reddit_id);
            holder.title = (TextView) row.findViewById(R.id.title_id);
            holder.icon = (ImageView) row.findViewById(R.id.image_id);
            holder.comments = (TextView) row.findViewById(R.id.comments_id);
            holder.progressBar = (ProgressBar) row.findViewById(R.id.progress_bar);
            row.setTag(holder);

        } else {
            holder = (PostModelHolder) row.getTag();
        }
        final PostModel model = ListPostModel.get(position);

        holder.title.setText(model.getTitle());
        holder.subReddit.setText(model.getSubreddit());
        holder.created.setText(setTime(String.valueOf(model.getCreated())));
        holder.author.setText(model.getAuthor());
        holder.comments.setText(String.valueOf(model.getComments()));
        if (model.getIcon().length > 0) {
            holder.icon.setImageBitmap(model.getImage(model.getIcon()));
            holder.progressBar.setVisibility(View.GONE);
            return row;

        }
        if (model.getThumbnail() != null && !mbusy && !model.isDownload()) {
            DownloadImageTask downloadImageTask = new DownloadImageTask(holder, model);
            String thumbnail = model.getThumbnail();
            downloadImageTask.execute(thumbnail);
        }

        ImageButton bv_up = (ImageButton) row.findViewById(R.id.row_up);
        ImageButton bv_down = (ImageButton) row.findViewById(R.id.row_down);

        if (NewsActivity.login){
            bv_up.setVisibility(View.VISIBLE);
            bv_down.setVisibility(View.VISIBLE);

            bv_up.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Logueado, votar POSITIVO!", Toast.LENGTH_SHORT).show();
                }
            });

            bv_down.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Logueado, votar NEGATIVO!", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            bv_up.setVisibility(View.GONE);
            bv_down.setVisibility(View.GONE);
        }

        return row;
    }


    private String setTime(String time) {
        String timestamp = String.valueOf(time);
        Date createdOn = new Date(Long.parseLong(timestamp));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(createdOn);

        return String.valueOf(formattedDate);

    }

    private class PostModelHolder {
        TextView title;
        TextView subReddit;
        TextView created;
        TextView author;
        ImageView icon;
        TextView comments;
        ProgressBar progressBar;
    }



    @Override
    public boolean isEmpty() {
        return ListPostModel.isEmpty();
    }


    private class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {
        PostModelHolder holder = null;
        PostModel model;


        public DownloadImageTask(PostModelHolder holder, PostModel model) {
            this.holder = holder;
            this.model = model;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            holder.progressBar.setVisibility(View.VISIBLE);
            model.setDownload(true);
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            System.out.println(url);
            Bitmap bitmap = null;
            bitmap = downloadBitmap(url);
            byte[] image = model.getBytes(bitmap);
            model.setIcon(image);
            db.updateImage(model);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                holder.icon.setImageBitmap(result);
            }
            holder.progressBar.setVisibility(View.GONE);
        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        final String DEFAULT_URL_REDDIT_ICON = "http://cdn.revistagq.com/uploads/images/thumbs/201525/reddit_5253_645x485.png";

        try {
            if (!URLUtil.isValidUrl(url)){
                // Default thumbnail
                url = DEFAULT_URL_REDDIT_ICON;
            }
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            assert urlConnection != null;
            urlConnection.disconnect();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}
