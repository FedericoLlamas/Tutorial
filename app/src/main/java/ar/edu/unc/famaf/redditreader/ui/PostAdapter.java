package ar.edu.unc.famaf.redditreader.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by nahuelseiler on 10/10/16.
 */

public class PostAdapter extends ArrayAdapter<PostModel> {

    int rs_id;
    private List<PostModel> model_list;

    public PostAdapter(Context context, int textViewResourceId, List<PostModel> postlist) {
        super(context, textViewResourceId);
        model_list = postlist;
        rs_id = textViewResourceId;
    }

    static class ViewHolder {
        TextView title;
        TextView author;
        TextView date;
        TextView comments;
        ImageView image;
    }

    @Override
    public int getCount() {
        return model_list.size();
    }

    @Override
    public int getPosition(PostModel item) {
        return model_list.indexOf(item);
    }

    @Override
    public PostModel getItem(int position) {
        return model_list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(rs_id, parent, false);

            PostModel post_model = model_list.get(position);

            holder.title = (TextView) convertView.findViewById(R.id.title_id);
            holder.author = (TextView) convertView.findViewById(R.id.author_id);
            holder.date = (TextView) convertView.findViewById(R.id.date_id);
            holder.comments = (TextView) convertView.findViewById(R.id.com_num_id);
            holder.image = (ImageView) convertView.findViewById(R.id.image_id);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        PostModel post_model = model_list.get(position);

        TextView postTitle = (TextView) convertView.findViewById(R.id.title_id);
        TextView postAuthor= (TextView) convertView.findViewById(R.id.author_id);
        TextView postDate = (TextView) convertView.findViewById(R.id.date_id);
        TextView postComments = (TextView) convertView.findViewById(R.id.com_num_id);
        ImageView postImage = (ImageView) convertView.findViewById(R.id.image_id);

        holder.title.setText(post_model.getTitle());
        holder.author.setText(post_model.getAuthor());
        holder.date.setText(post_model.getDate());
        holder.comments.setText(post_model.getComments());
        holder.image.setImageResource(post_model.getImage());

        return convertView;
    }

    @Override
    public boolean isEmpty() {
        return model_list.isEmpty();
    }


}
