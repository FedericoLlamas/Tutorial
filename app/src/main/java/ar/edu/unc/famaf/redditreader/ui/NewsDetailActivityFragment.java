package ar.edu.unc.famaf.redditreader.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;

public class NewsDetailActivityFragment extends Fragment {
    private PostModel postModel;
    private int clicks=0;
    private OnFragmentInteractionListener mListener;

    public NewsDetailActivityFragment() {
    }


    public static NewsDetailActivityFragment newInstance(PostModel post) {

        NewsDetailActivityFragment fragment = new NewsDetailActivityFragment();
        Bundle args = new Bundle();
        args.putSerializable("post", post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postModel = (PostModel) getArguments().getSerializable("post");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int score= postModel.getScore();
        View view = inflater.inflate(R.layout.fragment_news_detail_activity, container, false);

        TextView text1 = (TextView) view.findViewById(R.id.detail_date_id);
        text1.setText(String.valueOf(postModel.getCreated()));

        TextView text2 = (TextView) view.findViewById(R.id.detail_author_id);
        text2.setText(postModel.getAuthor());

        TextView text3 = (TextView) view.findViewById(R.id.detail_author_id);
        text3.setText(postModel.getSubreddit());

        TextView text4 = (TextView) view.findViewById(R.id.detail_title_id);
        text4.setText(postModel.getTitle());

        TextView text5 = (TextView) view.findViewById(R.id.detail_url_id);
        text5.setText(postModel.getUrl());
        text5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri myUri = Uri.parse(postModel.getUrl());
                mListener.onFragmentInteraction(myUri, null);
            }
        });

        ImageView image = (ImageView) view.findViewById(R.id.detail_img_id);
        image.setImageBitmap(getImage(postModel.getIcon()));

        PostModelHolder holder = new PostModelHolder();
        holder.score=(TextView) view.findViewById(R.id.score_id);
        holder.score.setText(String.valueOf(postModel.getScore()));

        holder.up = (ImageButton) view.findViewById(R.id.detail_up_id);
        holder.down = (ImageButton) view.findViewById(R.id.detail_down_id);

        if(NewsActivity.LOGIN && postModel.getClickup() == 1){
            holder.up.setBackgroundColor(Color.DKGRAY);
            score=postModel.getScore()-1;
        }
        if(NewsActivity.LOGIN && postModel.getClickdown() == 1){
            holder.down.setBackgroundColor(Color.DKGRAY);
            score=postModel.getScore()+1;
        }

        final VoteAction button = new VoteAction(postModel,holder, NewsActivityFragment.db   , view.getContext(), clicks, score);

        holder.up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                button.ButtonBehavior("1");
                if(NewsActivity.LOGIN) {
                    mListener.onFragmentInteraction(null, postModel);
                }
            }
        });

        holder.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                button.ButtonBehavior("-1");
                if(NewsActivity.LOGIN){
                    mListener.onFragmentInteraction(null, postModel);
                }
            }
        });

        return view;
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
            mListener.onFragmentInteraction(uri, null);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri, PostModel post);
    }

}
