package ar.edu.unc.famaf.redditreader.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;

import static android.os.AsyncTask.execute;

/**
 * Created by nahuelseiler on 10/10/16.
 */

public class PostAdapter extends ArrayAdapter<PostModel> {

    int rs_id;
    private List<PostModel> model_list;

    public PostAdapter(Context context, int textViewResourceId, List<PostModel> postlist) {
        super(context, textViewResourceId);
        model_list = postlist;
        rs_id = textViewResourceId;
    }

    static class ViewHolder {
        TextView title;
        TextView author;
        TextView date;
        TextView comments;
        ImageView image;
    }

    @Override
    public int getCount() {
        return model_list.size();
    }

    @Override
    public int getPosition(PostModel item) {
        return model_list.indexOf(item);
    }

    @Override
    public PostModel getItem(int position) {
        return model_list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(rs_id, parent, false);
        }
        if (convertView.getTag()==null) {
            PostModel post_model = model_list.get(position);
            holder  = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title_id);
            holder.author = (TextView) convertView.findViewById(R.id.author_id);
            holder.date = (TextView) convertView.findViewById(R.id.date_id);
            holder.comments = (TextView) convertView.findViewById(R.id.com_num_id);
            holder.image = (ImageView) convertView.findViewById(R.id.image_id);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        PostModel post_model = model_list.get(position);

        holder.title.setText(post_model.getTitle());
        holder.author.setText(post_model.getAuthor());
        holder.date.setText(post_model.getDate());
        holder.comments.setText(post_model.getComments());

        if (holder.image != null) {
            new DownLoadImageAsyncTask(holder.image).execute(post_model.getUrl());
        }
        //String bitmap = "https://www.google.com.ar/intl/en_ALL/images/logos/images_logo_lg.gif";

        return convertView;
    }

    class DownLoadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public DownLoadImageAsyncTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getDrawable(R.drawable.icono);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }

        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
                try {
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
                Log.e("MYAPP", "exception", e);
                Log.w("ImageDownloader", "Error downloading image from " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }



    @Override
    public boolean isEmpty() {
        return model_list.isEmpty();
    }


}
