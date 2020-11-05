package com.app.root.globalpunjabtv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.root.globalpunjabtv.Adapter.ImageSlider;
import com.app.root.globalpunjabtv.Adapter.RelatedNewsAdapter;
import com.app.root.globalpunjabtv.inteface.RelatednewsSend;
import com.app.root.globalpunjabtv.models.CatGetSet;
import com.app.root.globalpunjabtv.models.PostGetSet;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivitySinglePost extends AppCompatActivity implements RelatednewsSend {
    int id;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String lang;
    RecyclerView rv_relatedposts;
    List<CatGetSet> catGetSetList = new ArrayList<>();
    RelatedNewsAdapter rta;
    TextView post_title;
    TextView post_content, tv_author, tv_date;
    //ImageView image;
    // ProgressDialog progressDialog;
    List<PostGetSet> list2 = new ArrayList<>();
    String singleposturl, relatedurl, catname, next_catname,
            titlename, postcontent,post_date,author,next_title,post_link;
    Intent intent;
    int nextnewsid, previousnewsid, next_id;
    LinearLayout iv_nextnews, iv_previousnews;
    SpinKitView spin_kit6;
    boolean status = false;
    ImageView iv_share,iv_backbutton;
    String imagepath;
TextView tv_related_text;
    private static ViewPager mPager;
    ImageSlider imageSlideradapter;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    List<String> imageslist = new ArrayList<>();

    String[] strimages = new String[100];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);


        mPager = (ViewPager) findViewById(R.id.viewpager);
        spin_kit6 = findViewById(R.id.spin_kit6);
        tv_related_text = findViewById(R.id.tv_related_text);
        post_title = findViewById(R.id.post_title);
        iv_backbutton = findViewById(R.id.iv_backbutton);
        iv_share = findViewById(R.id.iv_share);
        tv_author = findViewById(R.id.tv_author);
        tv_date = findViewById(R.id.tv_date);
        rv_relatedposts = findViewById(R.id.rv_relatedposts);
        post_content = findViewById(R.id.post_content);
        //image = (ImageView) findViewById(R.id.image);
        iv_nextnews = findViewById(R.id.iv_nextnews);
        iv_previousnews = findViewById(R.id.iv_previousnews);
        //progressDialog = new ProgressDialog(SinglePostnavigationActivity.this);

        sharedPreferences = getSharedPreferences("Categories", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        lang = sharedPreferences.getString("language", "");

        if (lang.equals("punjabi")) {
            singleposturl = "https://globalpunjabtv.com/api/singlepost.php?id=";


        } else {
            singleposturl = "http://globalenglishnews.com/singlepost.php?id=";

        }

        id = getIntent().getIntExtra("postid", 0);
        catname = getIntent().getStringExtra("singlecatname");
        titlename = getIntent().getStringExtra("post_titlename");

        if (isConnected(this)) {
            spin_kit6.setVisibility(View.VISIBLE);

            try {
                loadSingleData(id);
                loadRelatedPosts(id, catname,post_link);
            } catch (Exception e) {
                Log.d("msg", "newssend1: "+e.toString());

            }


            //new SinglePostData().execute(id);
        }


        iv_nextnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spin_kit6.setVisibility(View.VISIBLE);

                try {
                    loadSingleData(nextnewsid);

                    loadRelatedPosts(next_id, next_catname,post_link);
                } catch (Exception e) {
                    Log.d("msg", "newssend2: "+e.toString());

                }

            }
        });

        iv_previousnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previousnewsid == 0) {
                    Toast.makeText(ActivitySinglePost.this, "No previous post", Toast.LENGTH_SHORT).show();
                } else {
                    spin_kit6.setVisibility(View.VISIBLE);

                    try {
                        loadSingleData(previousnewsid);

                        loadRelatedPosts(next_id, next_catname,post_link);
                    } catch (Exception e) {
                        Log.d("msg", "newssend3: "+e.toString());

                    }
                }

            }
        });

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("msg", "ShareLink: "+post_link);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, post_link);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });


        iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }


    public void loadSingleData(final int id) {
        imageslist.clear();
        strimages = new String[100];
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.GET, singleposturl + id
                ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);

                            JSONArray array = object.getJSONArray("postlist");

                            JSONArray array2 = object.getJSONArray("content_images");

                            for (int z = 0; z < array2.length(); z++) {
                                JSONObject j3 = array2.getJSONObject(z);
                                String guide = j3.getString("guid");
                                imageslist.add(guide);
                                strimages[z]=guide;
                            }


                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);

                                post_link=jsonObject.getString("singlepostlink");

                                next_title = jsonObject.getString("post_title");

                                nextnewsid = jsonObject.getInt("next_post");

                                previousnewsid = jsonObject.getInt("previous_post");


                                next_catname = jsonObject.getString("catname");
                                next_id = jsonObject.getInt("postid");
                                post_date=jsonObject.getString("date");
                                author=jsonObject.getString("author_name");

                                //post_content.setText(jsonObject.getString("content"));
                                postcontent = jsonObject.getString("content");


                                //    progressDialog.dismiss();

                            }


                            // Picasso.with(getApplicationContext()).load(imagepath).into(image);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            spin_kit6.setVisibility(View.GONE);
                //            Toast.makeText(ActivitySinglePost.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            Log.d("msg", "newssend9: " + e.toString());
                            //progressDialog.dismiss();

                        }

                        post_title.setText(next_title);

                        try{
                            String newdate=parseDateToddMMyyyy(post_date);
                            tv_date.setText(newdate);
                        }
                        catch (Exception e){
                            Toast.makeText(ActivitySinglePost.this, "No data", Toast.LENGTH_SHORT).show();
                        }



                        tv_author.setText(author);

                      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            post_content.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
                        }
                       */
                        post_content.setText(postcontent);


                      /*  post_content.setText(new SpannableString(postcontent));
                        TextJustification.justify(post_content);*/


                        spin_kit6.setVisibility(View.GONE);

                        try{

                            imageSlideradapter=new ImageSlider(ActivitySinglePost.this,
                                    strimages);
                            mPager.setAdapter(imageSlideradapter);
                           // imageSlideradapter.notifyDataSetChanged();
                            /*NUM_PAGES = imageslist.size();

                            // Auto start of viewpager
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == NUM_PAGES) {
                                        currentPage = 0;
                                    }
                                    mPager.setCurrentItem(currentPage++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 3000, 3000);*/
                        }
                        catch (Exception e){
                            Log.d("msg", "newssend5: "+e.toString());
                            //Toast.makeText(ActivitySinglePost.this, "Next:"+e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progress.dismiss();
                spin_kit6.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(sr);

    }


    private void loadRelatedPosts(final int id, String name, String post_link) {
        list2.clear();

        if (lang.equals("punjabi")) {
            if (catname.equals("North America")) {
                catname = "ਅਮਰੀਕਾ";

            } else if (catname.equals("ਪਰਵਾਸੀ-ਖ਼ਬਰਾਂ")) {
                catname = "ਪਰਵਾਸੀ-ਖ਼ਬਰਾਂ";

            } else if (catname.equals("ਜੀਵਨ ਢੰਗ")) {
                catname = "ਜੀਵਨ-ਢੰਗ";

            } else if (name.equals("ਪੰਜਾਬ")) {
                name = "ਪੰਜਾਬ";


            } else if (name.equals("ਭਾਰਤ")) {
                name = "ਭਾਰਤ";


            } else if (name.equals("ਮਨੋਰੰਜਨ")) {
                name = "ਮਨੋਰੰਜਨ";


            } else if (name.equals("ਸੰਸਾਰ")) {
                name = "ਸੰਸਾਰ";


            } else if (name.equals("ਓਪੀਨੀਅਨ")) {
                name = "ਓਪੀਨੀਅਨ";


            }

            relatedurl = "http://globalpunjabtv.com/api/related_posts.php?category=" + name + "&id=" + id;

        } else {
            if (name.equals("Viral Stories")) {
                name = "viral";

            } else if (name.equals("North America")) {
                name = "canada";

            } else if (name.equals("Entertainment")) {
                name = "entertainment";
            } else if (name.equals("India")) {
                name = "india";

            } else if (name.equals("Lifestyle")) {
                name = "lifestyle";
            } else if (name.equals("Punjab")) {
                name = "punjab";

            } else if (name.equals("World")) {
                name = "world";
            } else if (name.equals("Opinion")) {
                name = "opinion";
            } else if (name.equals("Diaspora")) {
                name = "sikh-community";
            }
            relatedurl = "https://globalenglishnews.com/related_posts.php?id=" + id + "&category=" + name;

        }

        Log.d("msg", "newssend8: " + relatedurl);
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.GET, relatedurl
                ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            Log.d("msg", "newssend7: " + response.toString());

                            JSONArray array = object.getJSONArray("postlist");

                            if(array.length()>0){
                                tv_related_text.setVisibility(View.VISIBLE);

                                //Toast.makeText(CheckPagination.this, ""+page+","+array.length(), Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    PostGetSet postGetSet = new PostGetSet(jsonObject.getInt("postid"),
                                            jsonObject.getInt("catid"),
                                            jsonObject.getString("date"),
                                            jsonObject.getString("author_name"),
                                            jsonObject.getString("thumbnail"),
                                            jsonObject.getString("catname"),
                                            jsonObject.getString("post_title"),
                                            jsonObject.getString("singlepostlink"));
                                    list2.add(postGetSet);

                                }
                                rv_relatedposts.setLayoutManager(new LinearLayoutManager(
                                        ActivitySinglePost.this, LinearLayoutManager.VERTICAL, false));
                                rta = new RelatedNewsAdapter(ActivitySinglePost.this,
                                        list2,ActivitySinglePost.this);
                                rv_relatedposts.setAdapter(rta);
                            }
                            else {
                                tv_related_text.setVisibility(View.GONE);

                            }



                            // progress.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                           // Toast.makeText(ActivitySinglePost.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("msg", "newssend6: " + e.toString()+","+id);
                            // progress.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progress.dismiss();
                Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(sr);

    }


    @Override
    public void newssend(int postid, String category_name,String title_name) {
        spin_kit6.setVisibility(View.VISIBLE);
        try {
            loadSingleData(postid);

            loadRelatedPosts(postid, category_name,title_name);
        } catch (Exception e) {
            Log.d("msg", "newssend4: "+e.toString());
        }

    }

 public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
     String outputPattern = "MMM dd yyyy h:mm a";
     SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
