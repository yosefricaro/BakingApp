package com.example.yosef.bakingapp.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yosef.bakingapp.R;
import com.example.yosef.bakingapp.model.StaticRecipe;
import com.example.yosef.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {

    @BindView(R.id.video_view) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.leftButton) Button left;
    @BindView(R.id.rightButton) Button right;
    private SimpleExoPlayer player;

    private static final String PLAYWHENREADY = "PLAYWHENREADY";
    private boolean playWhenReady= true;

    private static final String CURRENTWINDOW = "CURRENTWINDOW";
    private int currentWindow;

    private static final String PLAYBACKPOSITION = "PLAYBACKPOSITION";
    private long playBackPosition;

    private String videoURL;
    private int position;
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null){
            playWhenReady = savedInstanceState.getBoolean(PLAYWHENREADY);
            currentWindow = savedInstanceState.getInt(CURRENTWINDOW);
            playBackPosition = savedInstanceState.getLong(PLAYBACKPOSITION);
        }
        View rootView = inflater.inflate(R.layout.step_cardview, container, false);
        ButterKnife.bind(this, rootView);

        final List<Step> ls = StaticRecipe.getRecipe().getSteps();
        position = getArguments().getInt(ARG_ITEM_ID);
        description.setText(ls.get(position).getDescription());
        videoURL = ls.get(position).getVideoURL();


        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position>0) {
                    description.setText(ls.get(--position).getDescription());
                    videoURL = ls.get(position).getVideoURL();
                    releasePlayer();
                    iniPlayer();
                }
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position<ls.size()) {
                    description.setText(ls.get(++position).getDescription());
                    videoURL = ls.get(position).getVideoURL();
                    releasePlayer();
                    iniPlayer();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            currentWindow = player.getCurrentWindowIndex();
            playBackPosition = player.getCurrentPosition();
        }
        outState.putBoolean(PLAYWHENREADY, playWhenReady);
        outState.putInt(CURRENTWINDOW, currentWindow);
        outState.putLong(PLAYBACKPOSITION, playBackPosition);
    }

    @Override
    public void onStart() {
        super.onStart();
        hideSystemUI();
        if (Util.SDK_INT > 23){
            iniPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();
        if (Util.SDK_INT <= 23 || player == null){
            iniPlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23){
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23){
            releasePlayer();
        }
    }

    private void iniPlayer(){
        if (videoURL == null) return;
        player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity())
                , new DefaultTrackSelector(), new DefaultLoadControl());
        mPlayerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playBackPosition);

        Uri uri = Uri.parse(videoURL);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource);
    }

    private MediaSource buildMediaSource(Uri uri){
        return new ExtractorMediaSource(uri
                , new DefaultHttpDataSourceFactory("ua")
                , new DefaultExtractorsFactory(), null, null);
    }

    private void releasePlayer(){
        if (player != null){
            playWhenReady = player.getPlayWhenReady();
            currentWindow = player.getCurrentWindowIndex();
            playBackPosition = player.getCurrentPosition();
            player.release();
            player = null;
        }
    }

    private void hideSystemUI() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
