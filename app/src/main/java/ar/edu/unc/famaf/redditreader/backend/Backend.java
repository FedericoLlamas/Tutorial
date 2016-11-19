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
    private boolean loadingDB = false;
    private CtrlListing ctrlListing;
    private boolean waiting = false;

    public static Backend getInstance() { return ourInstance;    }

    private Backend() {
        PostModelList = new ArrayList<>();
    }


    public void getTopPosts(int totalItemsCount, Context context, final TopPostIterator iterator){
        final ReeditDBHelper db= new ReeditDBHelper(context, totalItemsCount).open();

        if (!isConnected(context, totalItemsCount) || loadingDB ){
            if (isConnected(context, totalItemsCount) && (totalItemsCount!=0) && (totalItemsCount % 50) == 0 && !waiting) {
                ctrlListing.DriverData(iterator,totalItemsCount,db);
            }else if(!waiting){
                new DbLoadData() {
                    @Override
                    protected void onPostExecute(List<PostModel> list1) {
                        iterator.nextPosts(list1, db);
                    }
                }.execute(db);
            }
        }else{
            ctrlListing = new CtrlListing();
            ctrlListing.DriverData(iterator, 0, db);
        }

    }

    private class CtrlListing{
        private List<PostModel> list;
        private String after;
        private String before;

        public void DriverData(final TopPostIterator iterator, int offset, final ReeditDBHelper db){
            new GetTopPostsTask(null,0) {
                @Override
                protected void onPostExecute(Listing input) {
                    list= input.getPostModelList();
                    after=input.getAfter();
                    before=input.getBefore();

                    if(list!=null) {
                        db.upgrade();
                        new DbSaveData(list) {
                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                loadingDB = true;
                                new DbLoadData() {
                                    @Override
                                    protected void onPostExecute(List<PostModel> l) {
                                        super.onPostExecute(l);
                                        iterator.nextPosts(l, db);
                                        list.clear();
                                    }
                                }.execute(db);
                            }
                        }.execute(db);
                    }
                }
            }.execute("https://www.reddit.com/top/.json?limit=50");
        }
    }

    private boolean isConnected(Context context, int totalItemsCount) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setMessage("No Internet connection!");
            dialogBuilder.setCancelable(true).setTitle("Alert");
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            if (totalItemsCount == 0){
                dialogBuilder.create().show();
            }
            return false;
        }
        return true;
    }

    private class DbLoadData extends AsyncTask<ReeditDBHelper,Void,List<PostModel>> {
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

    private class DbSaveData extends AsyncTask<ReeditDBHelper,Void,Void> {
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
