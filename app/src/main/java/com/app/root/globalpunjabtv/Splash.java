package com.app.root.globalpunjabtv;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Splash extends AppCompatActivity {
    private SpinKitView progressBar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SharedPreferences sp_token;
    SharedPreferences.Editor ed_token;
    String token;
    String success,success1;
    String INSERT_TOKEN_PUNJABI="https://globalpunjabtv.com/wp-content/themes/sahifa/fcm_notification/insert_token.php";
    String INSERT_TOKEN_ENGLISH="https://globalenglishnews.com/wp-content/themes/sahifa/fcm_notification/insert_token.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        sharedPreferences = getSharedPreferences("Categories", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.clear();
        editor.commit();

        sp_token=getSharedPreferences("SaveTokenn",MODE_PRIVATE);
        ed_token=sp_token.edit();

        //final String token = SharedPreference.getInstance(this).getDeviceToken();
        //Log.d("msg", "FCMToken: "+token);

        String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        progressBar=(SpinKitView) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Log.d("msg", "PhoneDeviceId: "+androidId);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar=(SpinKitView) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);

            }
        }, 2000);


       /* if(!sp_token.getString("TokenFCM","").equals("")){
            token=sp_token.getString("TokenFCM","");
            Log.d("msg", "Refreshedtoken1: "+token);

            RequestQueue requestQueue=Volley.newRequestQueue(Splash.this);
            StringRequest stringRequest=new StringRequest(Request.Method.POST,
                    "https://globalenglishnews.com/wp-content/themes/sahifa/fcm_notification/insert_token.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("msg", "TokenResponsee: "+response);
                    Toast.makeText(Splash.this, ""+response, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("msg", "TokenResponsee1: "+error.toString());

                    Toast.makeText(Splash.this, ""+error.toString(), Toast.LENGTH_SHORT).show();

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map=new HashMap<>();
                    map.put("token","e5P1HO8K36Y:APA91bFkCKHacTl6S6wtJ5Cp_PTmeoZNQcqnh5CV3jdJ2ibVVehUiMJ49SxaYjLBctXjUtN10ZZN-Sz20PK7cOtDjgSE6GwFJafVm9Z03gi9OMMNqYk3RPDlxQb2bCpASkXDXeWayvlF");
                    return map;
                }
            };
            requestQueue.add(stringRequest);

        }*/





        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(8000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                      if(!sp_token.getString("TokenFCM","").equals("")){
                      token=sp_token.getString("TokenFCM","");
                     Log.d("msg", "Refreshedtoken1: "+token);

                             RequestQueue rq= Volley.newRequestQueue(Splash.this);
                             StringRequest srq=new StringRequest(Request.Method.POST, INSERT_TOKEN_PUNJABI, new Response.Listener<String>() {
                                  @Override
                                  public void onResponse(String response) {
                                      Log.d("msg", "Refreshedtoken2: "+response);
                                      try {
                                          JSONArray jsonArray=new JSONArray(response);
                                          for(int z=0;z<jsonArray.length();z++){
                                              JSONObject jsonObject=jsonArray.getJSONObject(z);
                                              success=jsonObject.getString("success");
                                              if(success.equals("1")){
                                                 hitEnglishApi();
                                              }
                                              else {
                                                  Toast.makeText(Splash.this, "Something went wrong..Please try again later", Toast.LENGTH_SHORT).show();
                                              }
                                              // Toast.makeText(Splash.this, ""+success, Toast.LENGTH_SHORT).show();

                                          }
                                      } catch (JSONException e) {
                                          e.printStackTrace();
                                      }


                                  }
                              }, new Response.ErrorListener() {
                                  @Override
                                  public void onErrorResponse(VolleyError error) {
                                      Log.d("msg", "TokenResponse2: "+error.toString());
                                      Toast.makeText(Splash.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                                  }
                              }){
                                  @Override
                                  protected Map<String, String> getParams() throws AuthFailureError {
                                      Map<String,String> map=new HashMap<>();
                                      map.put("token",token);
                                      return map;
                                  }
                              };
                              rq.add(srq);

                       }

                 /*   startActivity(new Intent(Splash.this, SwitchAppPage.class));
                    finish();*/
                }
            }
        };
        thread.start();


    }

    private void hitEnglishApi() {
        RequestQueue rq1= Volley.newRequestQueue(Splash.this);
        StringRequest srq1=new StringRequest(Request.Method.POST, INSERT_TOKEN_ENGLISH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("msg", "Refreshedtoken3: "+response);
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int z=0;z<jsonArray.length();z++){
                        JSONObject jsonObject=jsonArray.getJSONObject(z);
                        success1=jsonObject.getString("success");
                        Log.d("msg", "getParams3: "+success1);
                        Log.d("msg", "getParams6: "+token);
                        if(success1.equals("1")){
                            startActivity(new Intent(Splash.this, SwitchAppPage.class));
                            finish();
                        }
                        else {
                            Toast.makeText(Splash.this, "Something went wrong..Please try again later", Toast.LENGTH_SHORT).show();
                        }
                        //   Toast.makeText(Splash.this, ""+success1, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("msg", "TokenResponse4: "+error.toString());
                Toast.makeText(Splash.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("token",token);

                return map;
            }
        };
        rq1.add(srq1);
    }

   /* public class TokenSEnd extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("https://globalenglishnews.com/wp-content/themes/sahifa/fcm_notification/insert_token.php");
                URLConnection urlConnection = url.openConnection();
                urlConnection.setDoOutput(true);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                return bufferedReader.readLine();


            } catch (Exception e) {
                e.printStackTrace();
                Log.d("msg", "doInBackgroundError: "+e.toString());
                return "error" + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String catlist) {
            Log.e("TokenResponsee3", catlist);
            Toast.makeText(getApplicationContext(),""+catlist,Toast.LENGTH_LONG).show();


            super.onPostExecute(catlist);
        }
    }*/
}
