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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsDetailActivityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsDetailActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsDetailActivityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ICON = "icon";
    private static final String ARG_TITLE = "title";
    private static final String ARG_AUTHOR= "author";
    private static final String ARG_SUBREDDIT= "subreddit";
    private static final String ARG_DATE = "date";
    private static final String ARG_URL = "url";
    private static final String ARG_COMMENTS = "comment";


    // TODO: Rename and change types of parameters
    private byte[] icon;
    private String title;
    private String author;
    private String subreddit;
    private String date;
    private String url;
    private int comments;

    private OnFragmentInteractionListener mListener;

    public NewsDetailActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsDetailActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
