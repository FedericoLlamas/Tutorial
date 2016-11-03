package ar.edu.unc.famaf.redditreader.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
        adapter = new PostAdapter(getContext(), R.layout.row_layout, postLst1);

        if (isOnline()){

            //new GetTopPostsTask(postLst1, this).execute();

            //Abrimos la base de datos 'Models_table' en modo escritura
            ReeditDBHelper reedDbHelper =
                    new ReeditDBHelper(getContext(), "Models_table", null, 1);

            SQLiteDatabase db = reedDbHelper.getWritableDatabase();
            //Si hemos abierto correctamente la base de datos
            if(db != null)
            {
                //Insertamos 5 usuarios de ejemplo
                //new GetTopPostsTask(postLst1, this).execute();
                postLst1 = Backend.getInstance().getTopPosts();
                for(int i=0; i <= postLst1.size(); i++)
                {
                    //Generamos los datos
                    String title = postLst1.get(i).getTitle();
                    String author = postLst1.get(i).getAuthor();
                    String date = postLst1.get(i).getDate();
                    String comments = postLst1.get(i).getComments();
                    //int image = postLst1.get(i).getImage();
                    String image = "imagen";
                    //String url = postLst1.get(i).getUrl();

                    //Insertamos los datos en la tabla Usuarios
                    /*db.execSQL("INSERT INTO Models_table (title, author, date, comments, image) " +
                            "VALUES ("+ title +", "+ author +", "+ date +", "+ comments +", "+ image +")");*/
                    db.execSQL("INSERT INTO Models_table (title, author, date, comments, image) " +
                            "VALUES ('title', 'author', 'date','comments','image')");
                }

                //Cerramos la base de datos
                db.close();
            }
        }
        else{
            buildDialog(getActivity()).show();
        }
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
