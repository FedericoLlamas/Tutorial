package ar.edu.unc.famaf.redditreader.backend;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;

public class Backend {

    private static Backend ourInstance = new Backend();

    public static Backend getInstance() {
        return ourInstance;
    }

    private List<PostModel> postLst;

    private Backend() {}

    public List<PostModel> getTopPosts(){
        ArrayList<PostModel> list = new ArrayList<PostModel>();
        list.add(new PostModel("Titulo1", "Federico", "Jan", "1",R.drawable.icono));
        list.add(new PostModel("Titulo2", "Jorge", "Feb", "2", R.drawable.icono));
        list.add(new PostModel("Titulo3", "Juliana", "Mar", "3", R.drawable.icono));
        list.add(new PostModel("Titulo4", "Fabricio", "Apr", "4", R.drawable.icono));
        list.add(new PostModel("Titulo5", "Marcelo", "May", "5", R.drawable.icono));
        return list;
    }
}