package ar.edu.unc.famaf.redditreader.model;

import java.util.Date;

public class PostModel {

    private String title;
    private String author;
    private String date;
    private String comments;
    private int image;

    public PostModel(String title, String author, String date, String comments, int image) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.comments = comments;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}




