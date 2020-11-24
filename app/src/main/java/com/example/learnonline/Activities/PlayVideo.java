package com.example.learnonline.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.learnonline.Adapter.VideoAdapter;
import com.example.learnonline.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlayVideo extends AppCompatActivity {
    private ArrayList<String> videotitle;
    private ArrayList<String> videouri;
    private   String userid;
    private String timestamp;
    private RecyclerView recyclerView;
    VideoAdapter videoAdapter;
    private PlayerView player_view;
    private ImageView btFullScreen;
    boolean flag=false;
    SimpleExoPlayer simpleExoPlayer;
    DataSource.Factory datafactory;
    TextView title,heading;
    String p="title",head="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play_video);
        recyclerView=findViewById(R.id.recyclerview1);
        player_view=findViewById(R.id.player_view);

        videotitle=new ArrayList<>();
        videouri=new ArrayList<>();
        heading=findViewById(R.id.heading);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                player_view.getLayoutParams();
        params.width=params.MATCH_PARENT;
        params.height=(int)(200*getApplicationContext().getResources().getDisplayMetrics().density);
        player_view.setLayoutParams(params);

        btFullScreen=findViewById(R.id.bt_fullscreen);
         userid=getIntent().getStringExtra("userid");
         timestamp=getIntent().getStringExtra("timestamp");

        head=getIntent().getStringExtra("head");
        heading.setText(head);
        videoAdapter =new VideoAdapter(PlayVideo.this,videotitle,videouri,player_view);
        recyclerView.setAdapter(videoAdapter);

        btFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                            player_view.getLayoutParams();
                    params.width=params.MATCH_PARENT;
                    params.height=(int)(200*getApplicationContext().getResources().getDisplayMetrics().density);
                    player_view.setLayoutParams(params);

                    btFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.exo_controls_fullscreen_enter));

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    recyclerView.setVisibility(View.VISIBLE);

                    flag=false;

                }
                else{

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                            player_view.getLayoutParams();
                    params.width=params.MATCH_PARENT;
                    params.height=params.MATCH_PARENT;
                    player_view.setLayoutParams(params);
                    btFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    //set others invisible

                    recyclerView.setVisibility(View.GONE);

                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    flag=true;
                }
            }
        });
        LoadControl loadControl = new DefaultLoadControl();

        BandwidthMeter bandwidthMeter=new DefaultBandwidthMeter();

        TrackSelector trackSelector=new DefaultTrackSelector(
                new AdaptiveTrackSelection.Factory(bandwidthMeter));

        simpleExoPlayer= ExoPlayerFactory.newSimpleInstance(PlayVideo.this,trackSelector,loadControl);
        datafactory = new DefaultDataSourceFactory(this,Util.getUserAgent(this,"exoplayer_video"));

        simpleExoPlayer.addListener(new Player.DefaultEventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline,Object manifest,int reason) {
                super.onTimelineChanged(timeline,manifest,reason);
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups,TrackSelectionArray trackSelections) {
                super.onTracksChanged(trackGroups,trackSelections);
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                super.onLoadingChanged(isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady,int playbackState) {
                if(playbackState == Player.STATE_BUFFERING){
                    //progressBar.setVisibility(View.VISIBLE);
                }
                else if(playbackState ==Player.STATE_READY){
                    //progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                super.onRepeatModeChanged(repeatMode);
            }
        });
        GetInfo();

    }

    private void GetInfo() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Processing");
        progressDialog.show();



        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userid).child("Courses").child(timestamp).child("Video").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for(DataSnapshot ds:snapshot.getChildren()){
                    videouri.add(ds.child("url").getValue().toString());
                    videotitle.add(ds.child("title").getValue().toString());
                    i++;

                    videoAdapter.notifyDataSetChanged();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}
