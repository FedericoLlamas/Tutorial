package ar.edu.unc.famaf.redditreader.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.backend.Backend;
import ar.edu.unc.famaf.redditreader.backend.GetTopPostsTask;
import ar.edu.unc.famaf.redditreader.backend.ReeditDBHelper;
import ar.edu.unc.famaf.redditreader.model.PostModel;

import static android.R.id.list;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsActivityFragment extends Fragment {
    PostAdapter adapter;
    public NewsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View lv_view =  inflater.inflate(R.layout.fragment_news, container, false);
        List<PostModel> postLst1 = new ArrayList<PostModel>();

        ReeditDBHelper reedDbHelper =
                new ReeditDBHelper(getContext(), "POST_DATABASE", null, 1);

        List<PostModel> list_post = new ArrayList<PostModel>();

        if (isOnline()){
            SQLiteDatabase db = reedDbHelper.getWritableDatabase();

            if(db != null)
            {
                reedDbHelper.onUpgrade(db, 1, 2);

                //new GetTopPostsTask(postLst1, this).execute();
                //TODO borrar, usar un listener.
                postLst1 = Backend.getInstance().getTopPosts();
                for(int i=0; i <= postLst1.size()-1; i++)
                {
                    String title = postLst1.get(i).getTitle();
                    String author = postLst1.get(i).getAuthor();
                    String date = postLst1.get(i).getDate();
                    String comments = postLst1.get(i).getComments();
                    String url = "url";
                    //String url = postLst1.get(i).getUrl();

                    db.execSQL("INSERT INTO post_table (title, author, date, comments, url) " +
                            "VALUES ('"+title+"', '"+author+"', '"+date+"','"+comments+"','"+url+"')");
                }
            }
            Cursor cursor = db.rawQuery("select * from post_table", null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String author = cursor.getString(cursor.getColumnIndex("author"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    String comments = cursor.getString(cursor.getColumnIndex("comments"));
                    String url = cursor.getString(cursor.getColumnIndex("url"));
                    list_post.add(new PostModel(title, author, date, comments, url));
                    cursor.moveToNext();
                }
            }
            db.close();
        }
        else{
            buildDialog(getActivity()).show();
            SQLiteDatabase db = reedDbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from post_table", null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String author = cursor.getString(cursor.getColumnIndex("author"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    String comments = cursor.getString(cursor.getColumnIndex("comments"));
                    String url = cursor.getString(cursor.getColumnIndex("url"));
                    list_post.add(new PostModel(title, author, date, comments, url));
                    cursor.moveToNext();
                }
            }
            db.close();
        }
        adapter = new PostAdapter(getContext(), R.layout.row_layout, list_post);
        ListView list_view = (ListView) lv_view.findViewById(R.id.list_view_id);
        list_view.setAdapter(adapter);

        return lv_view;
    }

    public void notifyNewsRetrieved() {
        adapter.notifyDataSetChanged();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet connection.");
        builder.setMessage("You have no internet connection");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return builder;
    }

    public static byte[] getBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image)
    {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
