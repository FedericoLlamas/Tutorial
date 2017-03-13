package ar.edu.unc.famaf.redditreader.model;


import android.content.ContentValues;
import java.io.Serializable;

import ar.edu.unc.famaf.redditreader.backend.DBAdapter;


public class PostModel implements Serializable {
    private Integer id;
    private String author;
    private  String name;
    private String title;
    private String url;
    private byte[] icon= new byte[0];
    private String subreddit;
    private long created;
    private String thumbnail;
    private int comments;
    private boolean download=false;
    private int score;
    private int clickup = 0;
    private int clickdown = 0;

    public int getClickup() {
        return clickup;
    }

    public void setClickup(int click) {
        this.clickup = click;
    }

    public int getClickdown() {
        return clickdown;
    }

    public void setClickdown(int click) {
        this.clickdown = click;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public byte[] getIcon(){return icon;}

    public void setIcon(byte[] icon2){
        this.icon=icon2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public ContentValues Values(){
        ContentValues val= new ContentValues();
        val.put(DBAdapter.AUTHOR, author);
        val.put(DBAdapter.SUBREDDIT, subreddit);
        val.put(DBAdapter.TITLE, title);
        val.put(DBAdapter.CREATED, created);
        val.put(DBAdapter.THUMBNAIL, thumbnail);
        val.put(DBAdapter.SCORE, score);
        val.put(DBAdapter.COMMENTS,comments);
        val.put(DBAdapter.URL, url);
        val.put(DBAdapter.NAME,name);
        if (this.icon.length >0 ){
            val.put(DBAdapter.ICON, icon);
        }
        return  val;
    }
}
