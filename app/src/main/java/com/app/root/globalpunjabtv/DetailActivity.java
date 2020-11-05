package com.app.root.globalpunjabtv;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class DetailActivity extends AppCompatActivity {
    TextView post_title;
    TextView post_content;
    ImageView image;
    RecyclerView rv_photos;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String lang,singleposturl;
    int id;
    private ImageButton iv_back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        post_title = (TextView) findViewById(R.id.post_title);
        post_content = (TextView) findViewById(R.id.post_content);
        image = (ImageView) findViewById(R.id.image);
        iv_back_arrow=(ImageButton) findViewById(R.id.iv_back_arrow);
        progressDialog = new ProgressDialog(DetailActivity.this);
        //rv_photos = (RecyclerView) findViewById(R.id.rv_photos);
        sharedPreferences=getSharedPreferences("Categories",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        lang = sharedPreferences.getString("language","");
//        post_title.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);

        id = getIntent().getIntExtra("postid", 0);
        Log.e("D V", String.valueOf(id));
        if (isConnected(this)) {

            new SinglePostData().execute(id);
        }
        iv_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailActivity.this,Home.class);
                startActivity(intent);
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


    public class SinglePostData extends AsyncTask<Integer, String, String> {
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog.setMax(100);
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }


        @Override
        protected String doInBackground(Integer... params) {
            if(lang.equals("punjabi")){
                singleposturl="https://globalpunjabtv.com/api/singlepost.php?id="+id;
//                Html.fromHtml(singleposturl).toString().replaceAll("", "").trim();

                Log.e("singlpost",""+params[0]);
                try {
                    URL url = new URL(singleposturl);
                    Log.e("stringurl",singleposturl);
                    URLConnection urlConnection = url.openConnection();
                    urlConnection.setDoOutput(true);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    return bufferedReader.readLine();


                } catch (Exception e) {
                    e.printStackTrace();
                    return "error" + e.getMessage();

                }
            }
            else {

                singleposturl="http://globalenglishnews.com/singlepost.php?id="+id;

            try {
                URL url = new URL(singleposturl);
                Log.e("stringurl",singleposturl);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setDoOutput(true);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                return bufferedReader.readLine();


            } catch (Exception e) {
                e.printStackTrace();
                return "error" + e.getMessage();

            }
        }}

        @Override
        protected void onPostExecute(String postlist) {
            Log.e("response1", postlist);
            try {
                JSONObject object = new JSONObject(postlist);

                JSONArray array = object.getJSONArray("postlist");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    post_title.setText(jsonObject.getString("post_title"));
                    String imagepath=jsonObject.getString("image");
                    Picasso.with(getApplicationContext()).load(imagepath).into(image);
                    post_content.setText(jsonObject.getString("content"));
                    progressDialog.dismiss();

                }
            } catch (JSONException e) {
                e.printStackTrace();
                progressDialog.dismiss();

            }
        }
    }
}