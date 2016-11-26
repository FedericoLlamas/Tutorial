package ar.edu.unc.famaf.redditreader.backend;

import java.util.List;

import ar.edu.unc.famaf.redditreader.model.Listing;
import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by federico on 26/11/16.
 */

public class SaveLoadData {
    private List<PostModel> list;
    private String after;
    private String before;

    public void DataChange(final TopPostIterator iterator, int offset, final ReeditDBHelper db){
        new GetTopPostsTask(null,0) {
            @Override
            protected void onPostExecute(Listing input) {
                list= input.getPostModelList();
                after=input.getAfter();
                before=input.getBefore();

                if(list!=null) {
                    db.upgrade();
                    new Backend.DbSaveData(list) {
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            new Backend.DbLoadData() {
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
