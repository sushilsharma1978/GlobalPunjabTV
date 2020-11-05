package com.app.root.globalpunjabtv;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class SingleDetail extends Fragment {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_single_detail, container, false);

        post_title = (TextView) v.findViewById(R.id.post_title);
        post_content = (TextView) v.findViewById(R.id.post_content);
        image = (ImageView) v.findViewById(R.id.image);
//        post_content.setText(new SpannableString(post_content.getText()));
//        TextJustification.justify(post_content);
       // iv_back_arrow=(ImageButton) v.findViewById(R.id.iv_back_arrow);
        progressDialog = new ProgressDialog(getActivity());
        //rv_photos = (RecyclerView) findViewById(R.id.rv_photos);
        sharedPreferences=getActivity().getSharedPreferences("Categories",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        lang = sharedPreferences.getString("language","");
//        post_title.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);

        //id = getActivity().getIntent().getIntExtra("postid", 0);


        id=getArguments().getInt("POSTID");


        Log.e("D V", String.valueOf(id));
        if (isConnected(getContext())) {

            new SinglePostData().execute(id);
        }


        return v;
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
                    Picasso.with(getContext()).load(imagepath).into(image);
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
