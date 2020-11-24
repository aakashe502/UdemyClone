package com.example.learnonline.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class UploadVideos extends AppCompatActivity {
    private TextView choose,current;
    private Button loaded;
    private TextView title;
    private ArrayList<String> videotitle;
    private ArrayList<String> videouri;
    private Button upload;
    private RecyclerView recyclerView;
    private RelativeLayout linear;
    private VideoAdapter videoAdapter;
    private String tagg="1";
    private static final int VIDEO_PICK_GALLERY_CODE =1 ;
    PlayerView playerView;
    ProgressBar progressBar;
    ImageView btFullScreen;


    private FirebaseAuth firebaseAuth;

    SimpleExoPlayer simpleExoPlayer;
    boolean flag=false;
    DataSource.Factory datafactory;
    Uri image_uri=Uri.parse("https://i.imgur.com/7bMqysJ.mp4");
    private LinearLayout line1;
    String title1,type1,learnwhat,target,requirements,image,price,discounted,upiid;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_upload_videos);
        btFullScreen=findViewById(R.id.bt_fullscreen);
        line1=findViewById(R.id.line1);

        title=findViewById(R.id.title);
        choose=findViewById(R.id.choose);
        upload=findViewById(R.id.upload);
        playerView=findViewById(R.id.player_view);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                playerView.getLayoutParams();
        params.width=params.MATCH_PARENT;
        params.height=(int)(200*getApplicationContext().getResources().getDisplayMetrics().density);
        playerView.setLayoutParams(params);

        linear=findViewById(R.id.linear);

        firebaseAuth=FirebaseAuth.getInstance();
        loaded=findViewById(R.id.loaded);
        current=findViewById(R.id.current);

        videouri=new ArrayList<>();
        image_uri=null;

        recyclerView=findViewById(R.id.item_recycler);
        progressDialog = new ProgressDialog(this);

/*
 intent.putExtra("title",t);
           intent.putExtra("learnw",l);
           intent.putExtra("requirements",r);
           intent.putExtra("target",tar);
           intent.putExtra("type",typ);
           intent.putExtra("image",image_uri);
           intent.putExtra("price",price.getText().toString());
           intent.putExtra("discounted",discounted.getText().toString());
*/
        videotitle=new ArrayList<>();
        //getting intent values
        title1=getIntent().getStringExtra("title");
         type1=getIntent().getStringExtra("type");
         learnwhat=getIntent().getStringExtra("learnw");
         target=getIntent().getStringExtra("target");
         requirements=getIntent().getStringExtra("requirements");
         image=getIntent().getStringExtra("image");
         price=getIntent().getStringExtra("price");
         discounted=getIntent().getStringExtra("discounted");
         upiid=getIntent().getStringExtra("upiid");

         Log.i(tagg,"title "+title);
         Log.i(tagg,"type = "+type1);
         Log.i(tagg,"desc = "+learnwhat);
         Log.i(tagg,"target = "+target);
         Log.i(tagg,"image_uri"+image);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadVideo();
            }
        });

        videoAdapter =new VideoAdapter(UploadVideos.this,videotitle,videouri,playerView);
        recyclerView.setAdapter(videoAdapter);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(TextUtils.isEmpty(title.getText().toString())){
                   Toast.makeText(UploadVideos.this,"give appropriate title ",Toast.LENGTH_SHORT).show();
               }
               else if(image_uri==null){
                   Toast.makeText(UploadVideos.this,"select another image",Toast.LENGTH_SHORT).show();
               }
                else{
                   videouri.add(image_uri.toString());

                   videotitle.add(title.getText().toString());
                recyclerView.setAdapter(videoAdapter);
                Toast.makeText(UploadVideos.this,""+image_uri,Toast.LENGTH_SHORT).show();
                   Log.i(tagg,"url = "+image_uri.toString());

               title.setText("");
                image_uri=null;
                }


            }
        });
                    btFullScreen.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View view) {
                if(flag){

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                            playerView.getLayoutParams();
                    params.width=params.MATCH_PARENT;
                    params.height=(int)(200*getApplicationContext().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);

                    btFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.exo_controls_fullscreen_enter));

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    recyclerView.setVisibility(View.VISIBLE);
                    title.setVisibility(View.VISIBLE);
                    choose.setVisibility(View.VISIBLE);
                    upload.setVisibility(View.VISIBLE);
                    loaded.setVisibility(View.VISIBLE);
                    current.setVisibility(View.VISIBLE);
                    flag=false;

                }
                else{

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                            playerView.getLayoutParams();
                    params.width=params.MATCH_PARENT;
                    params.height=params.MATCH_PARENT;
                    playerView.setLayoutParams(params);
                    btFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    //set others invisible

                    recyclerView.setVisibility(View.GONE);
                    title.setVisibility(View.GONE);
                    choose.setVisibility(View.GONE);
                    upload.setVisibility(View.GONE);
                    loaded.setVisibility(View.GONE);
                    current.setVisibility(View.GONE);
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    flag=true;
                }
            }
        });

        playerView=findViewById(R.id.player_view);
        progressBar=findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        btFullScreen=findViewById(R.id.bt_fullscreen);

        final Uri videouri=Uri.parse(String.valueOf(image_uri));

        LoadControl loadControl = new DefaultLoadControl();

        BandwidthMeter bandwidthMeter=new DefaultBandwidthMeter();

        TrackSelector trackSelector=new DefaultTrackSelector(
                new AdaptiveTrackSelection.Factory(bandwidthMeter));

        simpleExoPlayer= ExoPlayerFactory.newSimpleInstance(UploadVideos.this,trackSelector,loadControl);
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
                    progressBar.setVisibility(View.VISIBLE);
                }
                else if(playbackState ==Player.STATE_READY){
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                super.onRepeatModeChanged(repeatMode);
            }
        }

        );

        loaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(UploadVideos.this);
                builder.setTitle("Are you Sure To Publish the COurse").setCancelable(true)
                        .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface,int i) {
                                Toast.makeText(UploadVideos.this,"working",Toast.LENGTH_SHORT).show();
                                UploadToFirebase();
                            }
                        })
                        .setNegativeButton("NO",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface,int i) {
                                Toast.makeText(UploadVideos.this,"action dismissed",Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });


    }
    String timestamp;
    private void UploadToFirebase() {
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        //title1,type1,learnwhat,target,requirements,image,price,discounted;
        timestamp = "" + System.currentTimeMillis();
        String filepath = "profile_images"+timestamp;
        Calendar calendar=Calendar.getInstance();
        final String currentdate= DateFormat.getInstance().format(calendar.getTime());

        StorageReference storageReference= FirebaseStorage.getInstance().getReference(filepath);
        storageReference.putFile(Uri.parse(image.toString())).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                       while (!uriTask.isSuccessful()) ;
                       Uri downloadImageUri = uriTask.getResult();
                       if (uriTask.isSuccessful()) {
                           final HashMap<String,Object> hashMap=new HashMap<>();
                           hashMap.put("title",""+title1);
                           hashMap.put("type",""+type1);
                           hashMap.put("rating",""+"0");
                           hashMap.put("studentnumber",""+"0");
                           hashMap.put("description",""+learnwhat);
                           hashMap.put("target",""+target);
                           hashMap.put("requirements",""+requirements);
                           hashMap.put("image",""+downloadImageUri);
                           hashMap.put("price",""+price);
                           hashMap.put("upiid",upiid);
                           hashMap.put("discounted",""+discounted);
                           hashMap.put("timestamp",""+timestamp);
                           hashMap.put("userid",firebaseAuth.getUid().toString());
                           hashMap.put("date",""+currentdate);
                           DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
                           reference.child(firebaseAuth.getUid()).child("Courses")
                           .child(timestamp.toString()).child("INFO").setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {

                                   Toast.makeText(UploadVideos.this,"info uploaded",Toast.LENGTH_SHORT).show();


                                   DatabaseReference reference=FirebaseDatabase.getInstance().getReference("TCourses");
                                   reference.child(type1).child("title").child("header_title").setValue("top courses in "+type1);

                                   reference.child(type1).child("panel").child(timestamp).child("INFO").setValue(hashMap).
                                           addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   UploadVideosToFirebaseInstructor();
                                                   Toast.makeText(UploadVideos.this,"completed",Toast.LENGTH_SHORT).show();
                                                   
                                                   progressDialog.dismiss();

                                               }
                                           })
                                           .addOnFailureListener(new OnFailureListener() {
                                               @Override
                                               public void onFailure(@NonNull Exception e) {
                                                   Toast.makeText(UploadVideos.this,"error",Toast.LENGTH_SHORT).show();
                                                   progressDialog.dismiss();
                                               }
                                           });

                               }
                           })
                                   .addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Toast.makeText(UploadVideos.this,"error",Toast.LENGTH_SHORT).show();
                                           progressDialog.dismiss();
                                       }
                                   });

                       }
                       else{
                           progressDialog.dismiss();
                           Toast.makeText(UploadVideos.this,"error happens",Toast.LENGTH_SHORT).show();
                       }



            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    }
                });







    }

    private void UploadVideosToFirebaseInstructor() {

                       for(int i=0;i<videotitle.size();i++){
                           final int p=i;
                           final HashMap<String,Object> hashMap = new HashMap<>();
                           StorageReference storageReference=FirebaseStorage.getInstance().getReference("videos/"+timestamp+"/"+p);
                           storageReference.putFile(Uri.parse(videouri.get(i).toString())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                               @Override
                               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                   Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                   while (!uriTask.isSuccessful()) ;
                                   Uri downloadvidewouri = uriTask.getResult();
                                   if (uriTask.isSuccessful())   {
                                       hashMap.put("url",downloadvidewouri.toString());
                                       hashMap.put("title",videotitle.get(p));

                                       DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users");
                                               reference1.child(firebaseAuth.getUid().toString()).child("Courses").child(timestamp.toString()).child("Video").
                                                       child("vid"+p).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                   @Override
                                                   public void onSuccess(Void aVoid) {
                                                       Toast.makeText(UploadVideos.this,"processing",Toast.LENGTH_SHORT).show();
                                                   }
                                               })
                                                       .addOnFailureListener(new OnFailureListener() {
                                                           @Override
                                                           public void onFailure(@NonNull Exception e) {
                                                               Toast.makeText(UploadVideos.this,"canceled upload of video",Toast.LENGTH_SHORT).show();
                                                           }
                                                       });


                                   }

                               }
                           })
                                   .addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Toast.makeText(UploadVideos.this,"video upload error",Toast.LENGTH_SHORT).show();
                                       }
                                   });


                       }

    }

    private void UploadToCourses() {


    }

    private void uploadVideo() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(intent,VIDEO_PICK_GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(((requestCode == VIDEO_PICK_GALLERY_CODE) && resultCode== RESULT_OK && (data != null))){
            image_uri=data.getData();

            Toast.makeText(this,"selected",Toast.LENGTH_SHORT).show();

        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.getPlaybackState();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();
    }
}
