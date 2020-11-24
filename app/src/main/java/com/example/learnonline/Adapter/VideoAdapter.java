package com.example.learnonline.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnonline.Activities.UploadVideos;
import com.example.learnonline.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> arrayList;
    public PlayerView playerView;
    private ArrayList<String> videouri;
    DataSource.Factory datafactory;
    SimpleExoPlayer simpleExoPlayer;



    public VideoAdapter(Context context,ArrayList<String> arrayList,ArrayList<String> cideouri,PlayerView videoView) {
        this.context = context;
        this.arrayList = arrayList;
        this.videouri=cideouri;
        this.playerView=videoView;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.video_row_recycler,parent,false);
        LoadControl loadControl = new DefaultLoadControl();

        BandwidthMeter bandwidthMeter=new DefaultBandwidthMeter();

        TrackSelector trackSelector=new DefaultTrackSelector(
                new AdaptiveTrackSelection.Factory(bandwidthMeter));

        simpleExoPlayer= ExoPlayerFactory.newSimpleInstance(context,trackSelector,loadControl);
        datafactory = new DefaultDataSourceFactory(context,Util.getUserAgent(context,"exoplayer_video"));

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                playerView.setPlayer(simpleExoPlayer);

                MediaSource videosourse = new ExtractorMediaSource.Factory(datafactory)
                        .createMediaSource(Uri.parse(videouri.get(position).toString()));

                playerView.setKeepScreenOn(true);
                String tag="1";
                holder.itemView.setBackgroundColor(R.color.yellowcolor);



                simpleExoPlayer.prepare(videosourse);
                simpleExoPlayer.setPlayWhenReady(true);
                if(simpleExoPlayer.getCurrentPosition()==simpleExoPlayer.getDuration()){
                    holder.itemView.setBackgroundColor(R.color.colorWhite);
                }



                //Toast.makeText(context,""+arrayList.get(position),Toast.LENGTH_SHORT).show();

            }
        });




        holder.textView.setText(arrayList.get(position).toString());

    }

    @Override
    public int getItemCount() {
        return videouri.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text);

            imageView=itemView.findViewById(R.id.img);
        }
    }
}
