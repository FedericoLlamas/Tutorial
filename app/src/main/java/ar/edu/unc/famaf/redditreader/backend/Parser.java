package ar.edu.unc.famaf.redditreader.backend;


import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.Listing;
import ar.edu.unc.famaf.redditreader.model.PostModel;

public class Parser {

    public Listing readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            Listing list = null;
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("data")) {
                    String before=null;
                    String after=null;
                    reader.beginObject();
                    while (reader.hasNext()) {
                        name = reader.nextName();
                        if (name.equals("children")) {
                            list = ReadChildren(reader);

                        }else if (name.equals("after")){
                            after=reader.nextString();
                        }else if(name.equals("before") && reader.peek()!= JsonToken.NULL){
                            before=reader.nextString();
                        } else {
                            reader.skipValue();
                        }
                    }
                    list.setBefore(before);
                    list.setAfter(after);
                    break;
                } else {
                    reader.skipValue();
                }
            }
            return list;
        } finally {
            reader.close();
        }

    }

    Listing ReadChildren(JsonReader reader) throws IOException {
        Listing listing = new Listing();

        reader.beginArray();
        while (reader.hasNext()) {
            listing.add(readMessage(reader));
        }
        reader.endArray();
        return listing;

    }

    public PostModel readMessage(JsonReader reader) throws IOException {
        PostModel obj = null;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("data")) {
                obj = ReadJson(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public PostModel ReadJson(JsonReader reader) throws IOException {
        PostModel post = new PostModel();
        post.setIcon(new byte[0]);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "title":
                    post.setTitle(reader.nextString());
                    break;
                case "subreddit":
                    post.setSubreddit(reader.nextString());
                    break;
                case "created":
                    post.setCreated(reader.nextInt());
                    break;
                case "author":
                    post.setAuthor(reader.nextString());
                    break;
                case "thumbnail":
                    post.setThumbnail(reader.nextString());
                    break;
                case "url":
                    post.setUrl(reader.nextString());
                    break;
                case "num_comments":
                    post.setComments(reader.nextInt());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return post;
    }



}