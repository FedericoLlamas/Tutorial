package ar.edu.unc.famaf.redditreader.model;

public class PostModel {

    private String title;
    private String author;
    private String date;
    private String comments;
    private String url;

    public PostModel(String title, String author, String date, String comments, String url) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.comments = comments;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}




