package com.app.root.globalpunjabtv;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.root.globalpunjabtv.Adapter.RecyclerViewAdapter;
import com.app.root.globalpunjabtv.models.PostGetSet;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    //  PostAdapter postAdapter;
    LinearLayoutManager llm;
    PostGetSet postGetSet;
    //RecyclerView rv_cat;
    //ProgressDialog progress;
    String strtext;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String lang, caturl, categoryname;
    EditText ed_search;
    // SendData sendData;

    //  private ArrayList<PostGetSet> arrayList = new ArrayList<>();
    SpinKitView spin_kit5;
    private boolean isLoading = false;

    private int currentPage = 1;

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<PostGetSet> rowsArrayList = new ArrayList<>();
    SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_cat);
        spin_kit5 = rootView.findViewById(R.id.spin_kit5);
        ed_search = rootView.findViewById(R.id.ed_search_all);
        spin_kit5.setVisibility(View.VISIBLE);
        //rv_cat.setHasFixedSize(true);
        // Log.d("response catlist", arrayList.toString());


       /* sendData=new SendData() {
            @Override
            public void send(int position, int id) {
                SingleDetail singleDetailfragment=new SingleDetail();
                Bundle bundle=new Bundle();
                bundle.putInt("POSTID",id);
                singleDetailfragment.setArguments(bundle);
                FragmentManager manager=getFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.frag_all,singleDetailfragment).commit();
            }
        };*/

       /* postAdapter = new PostAdapter(getContext(), arrayList, getFragmentManager());
        rv_cat.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();
        llm = new LinearLayoutManager(getActivity());
        rv_cat.setLayoutManager(llm);*/
        rowsArrayList.clear();
        sharedPreferences = getActivity().getSharedPreferences("Categories", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        strtext = sharedPreferences.getString("categoryid", "");
        categoryname = sharedPreferences.getString("categoryname", "");
        //  Toast.makeText(getContext(), "Category:"+categoryname, Toast.LENGTH_SHORT).show();


        lang = sharedPreferences.getString("language", "");


        if (isConnected(getContext())) {

            populateData(currentPage);
            initScrollListener();

        } else {
            Toast.makeText(getContext(), "No internet", Toast.LENGTH_SHORT).show();
        }

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recyclerViewAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setHasOptionsMenu(true);

        return rootView;
    }


    private void populateData(int page) {

        if (lang.equals("punjabi")) {
            if (categoryname.equals("North America")) {
                categoryname = "ਅਮਰੀਕਾ";
                caturl = "https://globalpunjabtv.com/api/post.php?page=" + page + "&category=ਅਮਰੀਕਾ";

            } else if (categoryname.equals("ਪਰਵਾਸੀ-ਖ਼ਬਰਾਂ")) {
                categoryname = "ਪਰਵਾਸੀ-ਖ਼ਬਰਾਂ";
                caturl = "https://globalpunjabtv.com/api/post.php?page=" + page + "&category=ਪਰਵਾਸੀ-ਖ਼ਬਰਾਂ";

            } else if (categoryname.equals("ਜੀਵਨ ਢੰਗ")) {
                categoryname = "ਜੀਵਨ-ਢੰਗ";
                caturl = "https://globalpunjabtv.com/api/post.php?page=" + page + "&category=ਜੀਵਨ-ਢੰਗ";

            } else if (categoryname.equals("ਪੰਜਾਬ")) {
                categoryname = "ਪੰਜਾਬ";
                caturl = "https://globalpunjabtv.com/api/post.php?page=" + page + "&category=ਪੰਜਾਬ";

            } else if (categoryname.equals("ਭਾਰਤ")) {
                categoryname = "ਭਾਰਤ";
                caturl = "https://globalpunjabtv.com/api/post.php?page=" + page + "&category=ਭਾਰਤ";

            } else if (categoryname.equals("ਮਨੋਰੰਜਨ")) {
                categoryname = "ਮਨੋਰੰਜਨ";
                caturl = "https://globalpunjabtv.com/api/post.php?page=" + page + "&category=ਮਨੋਰੰਜਨ";

            } else if (categoryname.equals("ਸੰਸਾਰ")) {
                categoryname = "ਸੰਸਾਰ";
                caturl = "https://globalpunjabtv.com/api/post.php?page=" + page + "&category=ਸੰਸਾਰ";

            } else if (categoryname.equals("ਓਪੀਨੀਅਨ")) {
                categoryname = "ਓਪੀਨੀਅਨ";
                caturl = "https://globalpunjabtv.com/api/post.php?page=" + page + "&category=ਓਪੀਨੀਅਨ";

            }

           /* else if(categoryname.equals("Live Tv")){
                caturl = "https://globalpunjabtv.com/api/post.php?page="+page+"&category=ਓਪੀਨੀਅਨ";
                Log.d("msg", "populateDataWorld1: "+caturl);


            }*/
        } else if (lang.equals("english")) {

            if (categoryname.equals("Viral Stories")) {
                categoryname = "viral";
                caturl = "http://globalenglishnews.com/post.php?page=" + page + "&category=viral";

            } else if (categoryname.equals("North America")) {
                categoryname = "canada";
                caturl = "http://globalenglishnews.com/post.php?page=" + page + "&category=canada";

            } else if (categoryname.equals("Entertainment")) {
                categoryname = "entertainment";

                caturl = "http://globalenglishnews.com/post.php?page=" + page + "&category=entertainment";

            } else if (categoryname.equals("India")) {
                categoryname = "india";
                caturl = "http://globalenglishnews.com/post.php?page=" + page + "&category=india";

            } else if (categoryname.equals("Lifestyle")) {
                categoryname = "lifestyle";
                caturl = "http://globalenglishnews.com/post.php?page=" + page + "&category=lifestyle";

            } else if (categoryname.equals("Punjab")) {
                categoryname = "punjab";
                caturl = "http://globalenglishnews.com/post.php?page=" + page + "&category=punjab";

            } else if (categoryname.equals("World")) {
                categoryname = "world";
                caturl = "http://globalenglishnews.com/post.php?page=" + page + "&category=world";

            } else if (categoryname.equals("Opinion")) {
                categoryname = "opinion";
                caturl = "http://globalenglishnews.com/post.php?page=" + page + "&category=opinion";

            }
           /* else if(categoryname.equals("Live Tv")){
                caturl = "http://globalenglishnews.com/post.php?page="+page+"&category=opinion";
                Log.d("msg", "populateDataWorld: "+caturl);

            }*/
        }
        RequestQueue rq = Volley.newRequestQueue(getContext());
        Log.d("msg", "populateurl: " + caturl);
        StringRequest sr = new StringRequest(Request.Method.GET,
                caturl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("postlist");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                postGetSet = new PostGetSet(jsonObject.getInt("postid"),
                                        jsonObject.getInt("catid"),
                                        jsonObject.getString("date"),
                                        jsonObject.getString("author_name"),
                                        jsonObject.getString("thumbnail"),
                                        jsonObject.getString("catname"),
                                        jsonObject.getString("post_title"),
                                        jsonObject.getString("singlepostlink"));
                                rowsArrayList.add(postGetSet);

                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), rowsArrayList);
                            recyclerView.setAdapter(recyclerViewAdapter);
                            spin_kit5.setVisibility(View.GONE);

                            // progress.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("msg", "onResponseErrorr2: " + e.toString());
                     //       Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            spin_kit5.setVisibility(View.GONE);

                            //   progress.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progress.dismiss();
                spin_kit5.setVisibility(View.GONE);

                Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
            }
        });
        rq.add(sr);

    }


    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //    super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


            }
        });


    }

    private void loadMore() {
        rowsArrayList.add(null);
        recyclerViewAdapter.notifyItemInserted(rowsArrayList.size() - 1);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    currentPage = currentPage + 1;

                    currentSize++;
                }
                // Toast.makeText(getContext(), "Page2:"+currentPage, Toast.LENGTH_SHORT).show();

                populateData(currentPage);
                initScrollListener();

                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 1000);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {

        super.onAttachFragment(childFragment);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {

        }
    }

    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.layout_search, menu);
        menu.findItem(R.id.action_search);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(recyclerViewAdapter!=null){
                    recyclerViewAdapter.getFilter().filter(s.toString());
                }
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
    }

}


