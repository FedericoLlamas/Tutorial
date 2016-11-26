package ar.edu.unc.famaf.redditreader.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ar.edu.unc.famaf.redditreader.R;

public class NewsDetailActivityFragment extends Fragment {
    private static final String ARG_ICON = "icon";
    private static final String ARG_TITLE = "title";
    private static final String ARG_AUTHOR= "author";
    private static final String ARG_SUBREDDIT= "subreddit";
    private static final String ARG_DATE = "date";
    private static final String ARG_URL = "url";
    private static final String ARG_COMMENTS = "comment";


    private byte[] icon;
    private String title;
    private String author;
    private String subreddit;
    private String date;
    private String url;
    private int comments;

    private OnFragmentInteractionListener mListener;

    public NewsDetailActivityFragment() {
    }

    public static NewsDetailActivityFragment newInstance(byte[] param1, String param2, String param3,
                                                         String param4, String param5, String param6,
                                                         int param7) {
        NewsDetailActivityFragment fragment = new NewsDetailActivityFragment();
        Bundle args = new Bundle();
        args.putByteArray(ARG_ICON, param1);
        args.putString(ARG_TITLE, param2);
        args.putString(ARG_AUTHOR, param3);
        args.putString(ARG_SUBREDDIT, param4);
        args.putString(ARG_DATE, param5);
        args.putString(ARG_URL, param6);
        args.putInt(ARG_COMMENTS, param7);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            icon = getArguments().getByteArray(ARG_ICON);
            title = getArguments().getString(ARG_TITLE);
            author = getArguments().getString(ARG_AUTHOR);
            subreddit = getArguments().getString(ARG_SUBREDDIT);
            date = getArguments().getString(ARG_DATE);
            url = getArguments().getString(ARG_URL);
            comments = getArguments().getInt(ARG_COMMENTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View detailView = inflater.inflate(R.layout.fragment_news_detail_activity, container, false);
        TextView text1 = (TextView) detailView.findViewById(R.id.detail_title_id );
        text1.setText(title);
        TextView text2 = (TextView) detailView.findViewById(R.id.detail_author_view);
        text2.setText(author);
        TextView text3 = (TextView) detailView.findViewById(R.id.detail_comments_id);
        text3.setText(String.valueOf(comments));
        TextView text4 = (TextView) detailView.findViewById(R.id.detail_subrredit_id);
        text4.setText(subreddit);
        ImageView image = (ImageView) detailView.findViewById(R.id.detail_image_view);
        image.setImageBitmap(getImage(icon));
        TextView text5 = (TextView) detailView.findViewById(R.id.detail_url_id);
        text5.setText(url);
        text5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri myUri = Uri.parse(url);
                mListener.onFragmentInteraction(myUri);
            }
        });
        TextView text6 = (TextView) detailView.findViewById(R.id.detail_date_id );
        text6.setText(setTime(date));
        return detailView;
    }
    private String setTime(String time) {
        String timestamp = String.valueOf(time);
        Date createdOn = new Date(Long.parseLong(timestamp));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(createdOn);

        return String.valueOf(formattedDate);

    }
    public static Bitmap getImage(byte[] image){
        Bitmap b=null;
        try{
            b= BitmapFactory.decodeByteArray(image, 0, image.length);
        }catch (OutOfMemoryError e){
            e.printStackTrace();
        }
        return b;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
