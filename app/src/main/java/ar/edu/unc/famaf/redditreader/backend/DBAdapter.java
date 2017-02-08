package ar.edu.unc.famaf.redditreader.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;



public class DBAdapter {
    private DatabaseHelper DBHelper;
    private SQLiteDatabase database;
    private int count;

    public static final String TABLE_NAME = "postModel";
    public static final String ID = "id";
    public static final String AUTHOR = "mAuthor";
    public static final String TITLE = "mTitle";
    public static final String SUBREDDIT ="mSubreddit";
    public static final String COMMENTS = "comments";
    public static final String SCORE = "score";
    public static final String CREATED = "mCreated";
    public static final String URL = "url";
    public static final String THUMBNAIL ="thumbnail";
    public static  final String ICON = "icon";
    public static  final String NAME = "name";

    public DBAdapter(Context ctx, int totalItemsCount) {
        this.DBHelper = new DatabaseHelper(ctx);
        this.count= totalItemsCount;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "reddit.db";



        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                        + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + NAME + " TEXT,"
                        + AUTHOR + " TEXT,"
                        + TITLE + " TEXT,"
                        + SUBREDDIT + " TEXT,"
                        + COMMENTS + " TEXT,"
                        + CREATED + " INTEGER,"
                        + SCORE + " TEXT,"
                        + THUMBNAIL + " TEXT,"
                        + URL + " TEXT,"
                        + ICON + " BLOB,"
                        + "UNIQUE (" + ID + "))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public void upgrade(){
        DBHelper.onUpgrade(database,1,2);
    }

    public void close() {
        DBHelper.close();
    }

    public DBAdapter Writable() throws SQLException {
        database = DBHelper.getWritableDatabase();
        return this;
    }

    public void updateData(PostModel postModel, String elem){
        if(!database.isOpen()){
            Writable();
        }
        ContentValues val = new ContentValues();
        if (elem == "SCORE"){
            val.put(SCORE, postModel.getScore());
        }
        else if(elem == "IMAGE"){
            val.put(ICON, postModel.getIcon());
        }
        else{
            close();
        }
        database.update(TABLE_NAME, val, ID + "=" + postModel.getId(),null);
        close();
    }

    public DBAdapter savePostModel(List<PostModel> listModel) {
        database = DBHelper.getWritableDatabase();
        for(int i=0; i < listModel.size(); i++){
            database.insert(TABLE_NAME, null,  listModel.get(i).Values());
            Cursor cursor = database.query(TABLE_NAME, null, AUTHOR + " LIKE ?",
                    new String[]{listModel.get(i).getAuthor()}, null, null, null);
            if (cursor != null){
                cursor.moveToFirst();
                listModel.get(i).setId(cursor.getInt(cursor.getColumnIndex(ID)));
                cursor.close();
            }
        }
        return this;
    }

    public List<PostModel> getAllDb(){
        List<PostModel> list = new ArrayList<PostModel>();
        String query = "SELECT  * FROM " + TABLE_NAME + " LIMIT 5 OFFSET "+ String.valueOf(count);
        try {
            Cursor cursor = database.rawQuery(query, null);
            if(cursor.moveToFirst()) {
                do {
                    PostModel postModel = new PostModel();
                    postModel.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                    postModel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID))));
                    postModel.setAuthor(cursor.getString(cursor.getColumnIndex(AUTHOR)));
                    postModel.setComments(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COMMENTS))));
                    postModel.setSubreddit(cursor.getString(cursor.getColumnIndex(SUBREDDIT)));
                    postModel.setCreated(cursor.getLong(cursor.getColumnIndex(CREATED)));
                    postModel.setScore(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SCORE))));
                    postModel.setThumbnail(cursor.getString(cursor.getColumnIndex(THUMBNAIL)));
                    postModel.setUrl(cursor.getString(cursor.getColumnIndex(URL)));
                    byte[] image = cursor.getBlob(cursor.getColumnIndex(ICON));
                    if (image != null) {
                        postModel.setIcon(image);
                    }
                    postModel.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                    list.add(postModel);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
        return list;
    }

}
