package ar.edu.unc.famaf.redditreader.backend;


import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;


import java.util.ArrayList;
import java.util.List;


import ar.edu.unc.famaf.redditreader.model.Listing;
import ar.edu.unc.famaf.redditreader.model.PostModel;

public class Backend {
    private static Backend ourInstance = new Backend();
    private List<PostModel> PostModelList=null;
    private boolean loading = false;
    private SaveLoadData ctrlData;
    private boolean waiting = false;
    private Conected conected = new Conected();

    public static Backend getInstance() { return ourInstance;    }

    private Backend() {
        PostModelList = new ArrayList<>();
    }


    public void getTopPosts(int totalItemsCount, Context context, final TopPostIterator iterator){
        final ReeditDBHelper db= new ReeditDBHelper(context, totalItemsCount).open();

        if (!conected.isConnected(context, totalItemsCount) || loading ){
            if (conected.isConnected(context, totalItemsCount) && (totalItemsCount!=0) && (totalItemsCount % 50) == 0 && !waiting) {
                ctrlData.DataChange(iterator,totalItemsCount,db);
            }else if(!waiting){
                new DbLoadData() {
                    @Override
                    protected void onPostExecute(List<PostModel> list1) {
                        iterator.nextPosts(list1, db);
                    }
                }.execute(db);
            }
        }else{
            ctrlData = new SaveLoadData();
            ctrlData.DataChange(iterator, 0, db);
            loading = true;
        }

    }

    static class DbLoadData extends AsyncTask<ReeditDBHelper,Void,List<PostModel>> {
        @Override
        protected List<PostModel> doInBackground(ReeditDBHelper... dbAdapters) {
            List<PostModel> list=dbAdapters[0].getAllDb();
            dbAdapters[0].close();
            return list;
        }

        @Override
        protected void onPostExecute(List<PostModel> list1){
            super.onPostExecute(list1);
        }
    }

    static class DbSaveData extends AsyncTask<ReeditDBHelper,Void,Void> {
        private List<PostModel> list;

        public DbSaveData(List<PostModel> lst){
            list=lst;
        }

        @Override
        protected Void doInBackground(ReeditDBHelper... dbAdapters) {
            dbAdapters[0].savePostModel(list);
            return null;
        }
    }
}
