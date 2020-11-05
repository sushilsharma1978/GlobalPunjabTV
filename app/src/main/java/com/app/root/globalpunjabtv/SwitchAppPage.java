package com.app.root.globalpunjabtv;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SwitchAppPage extends AppCompatActivity {
    //    TextView tv_email;
    CardView btn_english,btn_punjabi;
    CardView btn_livetv;
    ImageView iv_amazon,iv_roku,iv_rss,iv_iostv,iv_ioss,iv_android;
    ImageButton live_imagebtn,facebook,instagram,youtube,twitter;
    private String Youtubeurl="https://www.youtube.com/user/GlobalPunjabTV";
    private static final String Fburl="https://www.facebook.com/GlobalPunjabTV/";
    private static final String Twitterurl="https://twitter.com/globalpunjabtv?lang=en";
    private static final String Instagramurl="https://www.instagram.com/globalpunjabtv/?hl=en";
    private static final String rokulink="https://channelstore.roku.com/en-gb/details/262254/globalpunjabtv-live";
    private static final String amazonlink="https://www.amazon.in/Global-Punjab-TV/dp/B07WG7P74S";
    private static final String iostvlink="https://apps.apple.com/us/app/globalpunjabtv/id1436892645";
    private static final String rsslink="https://globalpunjabtv.com/feed";
    private static final String androidlink="https://play.google.com/store/apps/details?id=com.globalpunjabtv&hl=en";
    private static final String ioslink="https://apps.apple.com/us/app/globalpunjabtv/id1436892645";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

   // FloatingActionButton live_main,live_tv,amazon_tv,ios_tv,roku_tv;
    Animation fab_open, fab_close, fab_clock, fab_anticlock;
    Boolean isOpen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_app_page);
        btn_english= findViewById(R.id.btn_english);
        btn_punjabi= findViewById(R.id.btn_punjabi);
        btn_livetv= findViewById(R.id.btn_livetv);
//        tv_email=(TextView)findViewById(R.id.tv_email);
//        live_imagebtn=(ImageButton)findViewById(R.id.live_imagebtn);
        facebook=(ImageButton)findViewById(R.id.facebook);
        instagram=(ImageButton)findViewById(R.id.instagram);
        youtube=(ImageButton)findViewById(R.id.youtube);
        twitter=(ImageButton)findViewById(R.id.twitter);
        twitter=(ImageButton)findViewById(R.id.twitter);
        //live_main = findViewById(R.id.live_main);
        iv_amazon = findViewById(R.id.iv_amazon);
        iv_roku = findViewById(R.id.iv_roku);
        iv_iostv = findViewById(R.id.iv_iostv);
        iv_rss = findViewById(R.id.iv_rss);
        iv_ioss=findViewById(R.id.iv_ioss);
        iv_android=findViewById(R.id.iv_android);

        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);


        sharedPreferences=getSharedPreferences("Categories", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
     /*   live_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {

                    live_tv.startAnimation(fab_close);
                    amazon_tv.startAnimation(fab_close);
                    ios_tv.startAnimation(fab_close);
                    roku_tv.startAnimation(fab_close);
                  //  live_main.startAnimation(fab_anticlock);
                    live_tv.setClickable(false);
                    amazon_tv.setClickable(false);
                    ios_tv.setClickable(false);
                    roku_tv.setClickable(false);
                    isOpen = false;
                } else {
                    live_tv.startAnimation(fab_open);
                    amazon_tv.startAnimation(fab_open);
                    ios_tv.startAnimation(fab_open);
                    roku_tv.startAnimation(fab_open);
                  //  live_main.startAnimation(fab_clock);
                    live_tv.setClickable(true);
                    amazon_tv.setClickable(true);
                    ios_tv.setClickable(true);
                    roku_tv.setClickable(true);
                    isOpen = true;
                }

            }
        });*/


        btn_livetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),LiveTvActivity.class);
                startActivity(intent);

            }
        });



        iv_amazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(amazonlink));
                startActivity(browserIntent);
           //     Toast.makeText(getApplicationContext(), "Amazon tv", Toast.LENGTH_SHORT).show();

            }
        });
        iv_roku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rokulink));
                startActivity(browserIntent);

            }
        });
        iv_iostv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(iostvlink));
                startActivity(browserIntent);

            }
        });
        iv_rss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rsslink));
                startActivity(browserIntent);

            }
        });

        iv_android.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(androidlink));
                startActivity(browserIntent);
            }
        });

        iv_ioss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ioslink));
                startActivity(browserIntent);
            }
        });




//for punjabi
        btn_punjabi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("language","punjabi");
                editor.commit();
                Intent i=new Intent(getApplicationContext(),Home.class);

                startActivity(i);
            }
        });
        btn_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("language","english");
                editor.commit();
                Intent i=new Intent(getApplicationContext(),Home.class);

                startActivity(i);
            }
        });
//        live_imagebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),LiveTvActivity.class);
//                startActivity(intent);
//            }
//        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Fburl));
                    startActivity(intent);
                } catch (Exception e)
                {
                }
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( Youtubeurl));
                    startActivity(intent);
                } catch (Exception e)
                {

                }

            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Instagramurl));
                    startActivity(intent);
                } catch (Exception e)
                {
                }

            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Twitterurl));
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        });
     /*   tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "info@globalpunjabtv.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                startActivity(Intent.createChooser(emailIntent, null));
            }
        });*/
    }
    @Override
    public void onBackPressed() {
        showDialogExit();
    }

    private void showDialogExit() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(SwitchAppPage.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.exit_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(SwitchAppPage.this);
        alertDialogBuilderUserInput.setTitle("Exit");
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialogBox, int id) {
                        finishAffinity();
                        System.exit(0);
                    }


                })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox,
                                                int id) {
                                dialogBox.cancel();
                            }
                        });
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }
}