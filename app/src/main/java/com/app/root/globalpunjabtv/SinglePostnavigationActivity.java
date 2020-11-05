package com.app.root.globalpunjabtv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.app.root.globalpunjabtv.Adapter.RelatedNewsAdapter;
import com.app.root.globalpunjabtv.inteface.RelatednewsSend;
import com.app.root.globalpunjabtv.models.CatGetSet;
import com.app.root.globalpunjabtv.models.PostGetSet;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class SinglePostnavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,RelatednewsSend {
    int id;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String lang;
    NavigationView navigationView;
    String caturl;
    RecyclerView rv_relatedposts;
    List<CatGetSet> catGetSetList = new ArrayList<>();
    RelatedNewsAdapter rta;
    TextView post_title;
    TextView post_content, tv_author, tv_date;
    //ImageView image;
    // ProgressDialog progressDialog;
    List<PostGetSet> list2 = new ArrayList<>();
    String singleposturl, relatedurl, catname, next_catname, posttitle, postcontent, sharelink;
    Intent intent;
    int nextnewsid, previousnewsid, next_id;
    LinearLayout iv_nextnews, iv_previousnews;
    SpinKitView spin_kit6;
    boolean status = false;
    ImageView iv_share;
    String imagepath;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    List<String> imageslist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_postnavigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        mPager = (ViewPager) findViewById(R.id.viewpager);
        spin_kit6 = findViewById(R.id.spin_kit6);
        id = getIntent().getIntExtra("postid", 0);
        catname = getIntent().getStringExtra("singlecatname");

        new CatData().execute();


        post_title = findViewById(R.id.post_title);
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
            caturl = "http://globalpunjabtv.com/api/cat_by_order.php";
            Log.e("enter punjabi", caturl);
            // relatedurl="http://globalpunjabtv.com/api/related_posts.php?category="+catname+"&id="+id;
            singleposturl = "https://globalpunjabtv.com/api/singlepost.php?id=";

        } else {


            caturl = "http://globalenglishnews.com/cat_by_order.php";
            // relatedurl="https://globalenglishnews.com/related_posts.php?id="+id+"&category="+catname;
            Log.d("msg", "checkRelatedPosts4: " + relatedurl);
            singleposturl = "http://globalenglishnews.com/singlepost.php?id=";

        }


        if (isConnected(this)) {
            spin_kit6.setVisibility(View.VISIBLE);
            loadSingleData(id);
            try {

                loadRelatedPosts(id, catname);
            } catch (Exception e) {
            }
            sharelink = singleposturl + id;

            //new SinglePostData().execute(id);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        iv_nextnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spin_kit6.setVisibility(View.VISIBLE);
                loadSingleData(nextnewsid);

                try {
                    loadRelatedPosts(next_id, next_catname);
                } catch (Exception e) {
                }
                sharelink = singleposturl + next_id;
            }
        });

        iv_previousnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previousnewsid == 0) {
                    Toast.makeText(SinglePostnavigationActivity.this, "No previous post", Toast.LENGTH_SHORT).show();
                } else {
                    spin_kit6.setVisibility(View.VISIBLE);
                    loadSingleData(previousnewsid);

                    try {
                        loadRelatedPosts(next_id, next_catname);
                    } catch (Exception e) {

                    }
                    sharelink = singleposturl + next_id;
                }

            }
        });

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharelink);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });

    }

    @Override
    public void newssend(int postid, String category_name, String title_name) {

    }

    public class CatData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(caturl);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setDoOutput(true);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                return bufferedReader.readLine();


            } catch (Exception e) {
                e.printStackTrace();
                return "error" + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String catlist) {
            Log.e("response", catlist);
            try {
//                Toast.makeText(getApplicationContext(),"Punjabi",Toast.LENGTH_LONG).show();
                catGetSetList.clear();
                CatGetSet catGetSett1 = new CatGetSet
                        ("0", "https://globalpunjabtv.com/api/post.php", "All");
                catGetSetList.add(catGetSett1);

                JSONObject object = new JSONObject(catlist);
                JSONArray array = object.getJSONArray("catorder");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    String catname = jsonObject.getString("catname");


                  /*  if (lang.equals("punjabi")) {
                        if (catname.equals("ਪੰਜਾਬ") || catname.equals("ਭਾਰਤ") || catname.equals("ਮਨੋਰੰਜਨ") ||
                                catname.equals("ਜੀਵਨ ਢੰਗ") || catname.equals("ਪਰਵਾਸੀ-ਖ਼ਬਰਾਂ") || catname.equals("North America") ||
                                catname.equals("ਸੰਸਾਰ") || catname.equals("ਓਪੀਨੀਅਨ")) {
                            CatGetSet catGetSett = new CatGetSet(jsonObject.getString("catid"),
                                    jsonObject.getString("catlink"), jsonObject.getString("catname"));

                            catGetSetList.add(catGetSett);

                        }
                    } else {
                        if (catname.equals("Punjab") || catname.equals("India") || catname.equals("Entertainment") ||
                                catname.equals("Lifestyle") || catname.equals("Viral Stories") || catname.equals("North America") ||
                                catname.equals("World") || catname.equals("Opinion")) {*/
                            CatGetSet catGetSett = new CatGetSet(jsonObject.getString("catid"),
                                    jsonObject.getString("catlink"), jsonObject.getString("catname"));


                            catGetSetList.add(catGetSett);

                      /*  }
                    }*/


                }

               /* CatGetSet catGetSett2 = new CatGetSet("100",
                        "", "Live Tv");
                catGetSetList.add(catGetSett2);*/


            } catch (JSONException e) {
                e.printStackTrace();
            }

            addMenuItemInNavMenuDrawer(catGetSetList);


            super.onPostExecute(catlist);
        }
    }

    private void addMenuItemInNavMenuDrawer(List<CatGetSet> catGetSetList) {
        final Menu menu = navigationView.getMenu();

        for (int i = 0; i < catGetSetList.size(); i++) {
            menu.add(0, Integer.parseInt(catGetSetList.get(i).getCatid()),
                    i, catGetSetList.get(i).getCatname());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int orderid = item.getOrder();
        if (item.getTitle().equals("All")) {

            Intent i = new Intent(SinglePostnavigationActivity.this, Home.class);
            i.putExtra("selected_item", orderid);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(i);
            finish();


        } /*else if (item.getTitle().equals("Live Tv")) {

            startActivity(new Intent(SinglePostnavigationActivity.this, LiveTvActivity.class));
            finish();
        }*/ else {

            Intent i = new Intent(SinglePostnavigationActivity.this, Home.class);
            i.putExtra("selected_item", orderid);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(i);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                            }


                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                posttitle = jsonObject.getString("post_title");

                                imagepath = jsonObject.getString("image");
                                nextnewsid = jsonObject.getInt("next_post");

                                previousnewsid = jsonObject.getInt("previous_post");

                                next_catname = jsonObject.getString("catname");
                                next_id = jsonObject.getInt("postid");

                                //post_content.setText(jsonObject.getString("content"));
                                postcontent = jsonObject.getString("content");
                                tv_author.setText(jsonObject.getString("author_name"));
                                tv_date.setText(jsonObject.getString("date"));
                                //    progressDialog.dismiss();

                            }

                            // Picasso.with(getApplicationContext()).load(imagepath).into(image);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            spin_kit6.setVisibility(View.GONE);
                            Log.d("msg", "previouspostt: " + e.toString());
                            //progressDialog.dismiss();

                        }

                        post_title.setText(posttitle);

                        post_content.setText(new SpannableString(postcontent));
                        TextJustification.justify(post_content);


                        spin_kit6.setVisibility(View.GONE);

                        try{
                           // mPager.setAdapter(new ImageSlider(SinglePostnavigationActivity.this, imageslist));
                          /*  NUM_PAGES = imageslist.size();

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
                            //Toast.makeText(SinglePostnavigationActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
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


    private void loadRelatedPosts(int id, String name) {
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

        Log.d("msg", "loadRelatedPosts: " + relatedurl);
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.GET, relatedurl
                ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("postlist");

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
                                    SinglePostnavigationActivity.this, LinearLayoutManager.VERTICAL, false));
                            rta = new RelatedNewsAdapter(SinglePostnavigationActivity.this,
                                    list2,SinglePostnavigationActivity.this);
                            rv_relatedposts.setAdapter(rta);

                            // progress.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(getContext(), "2"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("msg", "onResponseCheck: " + e.toString());
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



}
