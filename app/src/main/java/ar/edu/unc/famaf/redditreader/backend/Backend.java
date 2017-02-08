package ar.edu.unc.famaf.redditreader.backend;


import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Switch;


import java.util.ArrayList;
import java.util.List;


import ar.edu.unc.famaf.redditreader.model.Listing;
import ar.edu.unc.famaf.redditreader.model.PostModel;

public class Backend {
    private static Backend ourInstance = new Backend();

    private boolean loadingDB = false;
    private CtrlListing ctrlListing;
    private boolean waiting = false;
    private String NO_INTERNET_MSG = "No hay conexión a intenert.";

    public static Backend getInstance() { return ourInstance;    }

    private Backend() {
        List<PostModel> mListPostModel = new ArrayList<>();
    }

    public void getTopPosts(int countItem, Context context, final TopPostIterator iterator){
        final DBAdapter db = new DBAdapter(context, countItem).Writable();
        if (!isConnected(context) || loadingDB ){
            if (isConnected(context) && (countItem!=0) && (countItem % 50) == 0 && !waiting) {
                ctrlListing.control(iterator,countItem,db);
            }else if(!waiting){
                new DbLoadTask() {
                    @Override
                    protected void onPostExecute(List<PostModel> list1) {
                        iterator.nextPosts(list1, db);
                    }
                }.execute(db);
            }
        }else {
            ctrlListing = new CtrlListing();
            ctrlListing.init_process(iterator,db);
        }

    }

    private class CtrlListing{
        private List<PostModel> list;
        private String after;
        private String before;

        public void init_process(final TopPostIterator iterator, final DBAdapter db){
            //db.upgrade();
            new GetTopPostsTask(null,0) {
                @Override
                protected void onPostExecute(Listing input) {
                    list= input.getPostModelList();// java.lang.NullPointerException:
                    after=input.getAfter();
                    before=input.getBefore();

                    if(list.size()!=0) {
                        db.upgrade();
                        new DbSaveTask(list) {
                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                loadingDB = true;
                                //Cargo desde la base de datos la lista
                                new DbLoadTask() {
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

        public void control(final TopPostIterator iterator, int offset, final DBAdapter db){
            waiting=true;
            if(after!=null){
                new GetTopPostsTask(after,offset) {//pasarle after para conectarse
                    @Override
                    protected void onPostExecute(Listing input) {
                        list=input.getPostModelList();
                        after=input.getAfter();
                        before=input.getBefore();
                        if(list!=null) {
                            new DbSaveTask(list){
                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    new DbLoadTask() {
                                        @Override
                                        protected void onPostExecute(List<PostModel> l) {
                                            super.onPostExecute(l);
                                            iterator.nextPosts(l, db);
                                            waiting =false;
                                        }
                                    }.execute(db);
                                }
                            }.execute(db);
                        }
                    }
                }.execute("https://www.reddit.com/top/.json?limit=50");
            }
        }
    }


    private boolean isConnected(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(NO_INTERNET_MSG)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            return false;
        }
        return true;
    }

    public class DbLoadTask extends AsyncTask<DBAdapter,Void,List<PostModel>> {
        @Override
        protected List<PostModel> doInBackground(DBAdapter... dbAdapters) {
            List<PostModel> list=dbAdapters[0].getAllDb();
            dbAdapters[0].close();
            return list;
        }

        @Override
        protected void onPostExecute(List<PostModel> list1){
            super.onPostExecute(list1);
        }
    }

    public class DbSaveTask extends AsyncTask<DBAdapter,Void,Void> {
        private List<PostModel> list;

        public DbSaveTask(List<PostModel> lst){
            list=lst;
        }

        @Override
        protected Void doInBackground(DBAdapter... dbAdapters) {
            dbAdapters[0].savePostModel(list);
            return null;
        }
    }
}
