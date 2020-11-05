package com.app.root.globalpunjabtv;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;

import com.app.root.globalpunjabtv.Adapter.TopnewsSlider;

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
import com.app.root.globalpunjabtv.Adapter.CatAdapter;
import com.app.root.globalpunjabtv.Adapter.RecyclerViewAdapter;
import com.app.root.globalpunjabtv.models.CatGetSet;
import com.app.root.globalpunjabtv.models.PostGetSet;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BlankFragment extends Fragment  {

    //PostAdapter postAdapter;
    CatAdapter catAdapter;
    LinearLayoutManager llm;
    PostGetSet postGetSet;
    CatGetSet catGetSet;
    //RecyclerView rv;
    // ProgressDialog progress;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String lang, posturl, categoryname;
    private Parcelable recyclerViewState;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private boolean isLoading = false;
    EditText ed_search;
    private int currentPage = 1;
    SpinKitView spin_kit4;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<PostGetSet> rowsArrayList = new ArrayList<>();
    ArrayList<PostGetSet> toplist = new ArrayList<>();
    private static ViewPager topnews_slider;
    private static int newPage = 0;
    private static int NUM_PAGES = 0;
    SearchView searchView;

    //SendSearchData sendSearchData;
//SendData sendData;
    // private ArrayList<PostGetSet> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rview);
        ed_search = rootView.findViewById(R.id.ed_search);
        topnews_slider = rootView.findViewById(R.id.topnews_slider);
        // rv.setHasFixedSize(true);
        // Log.d("response", rowsArrayList.toString());


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

       /* postAdapter=new PostAdapter(getContext(),arrayList,getFragmentManager());
        rv.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();
        llm=new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);*/
        spin_kit4 = rootView.findViewById(R.id.spin_kit4);
        spin_kit4.setVisibility(View.VISIBLE);


        rowsArrayList.clear();
        toplist.clear();
        sharedPreferences = getActivity().getSharedPreferences("Categories", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        lang = sharedPreferences.getString("language", "");

        categoryname = sharedPreferences.getString("categoryname", "");


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
                if(recyclerViewAdapter!=null){
                    recyclerViewAdapter.getFilter().filter(s.toString());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setHasOptionsMenu(true);

        return rootView;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }


    private void populateData(int page) {
        toplist.clear();
        if (lang.equals("punjabi")) {
            posturl = "https://globalpunjabtv.com/api/post.php?page=" + page + "&category=all";
            Log.e("punjabi post", posturl);
        } else if (lang.equals("english")) {

            posturl = "http://globalenglishnews.com/post.php?page=" + page + "&category=all";

            Log.e("eng post", posturl);
        }

        RequestQueue rq = Volley.newRequestQueue(getContext());
        StringRequest sr = new StringRequest(Request.Method.GET,
                posturl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("postlist");
                            //Toast.makeText(CheckPagination.this, ""+page+","+array.length(), Toast.LENGTH_SHORT).show();
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
                                // toplist.add(postGetSet);


                            }

                            //sendSearchData.sendtoActivity(rowsArrayList);

                            toplist.addAll(rowsArrayList.subList(0, 5));

                            //  Toast.makeText(getContext(), ""+toplist.size(), Toast.LENGTH_SHORT).show();
                            topnews_slider.setAdapter(new TopnewsSlider(getActivity(), toplist));


                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), rowsArrayList);
                            recyclerView.setAdapter(recyclerViewAdapter);
                            spin_kit4.setVisibility(View.GONE);


                            NUM_PAGES = toplist.size();

                            // Auto start of viewpager
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (newPage == NUM_PAGES) {
                                        newPage = 0;
                                    }
                                    topnews_slider.setCurrentItem(newPage++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 3000, 3000);

                            //progress.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //     Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                            Log.d("msg", "onResponseCheck1: " + e.toString());
                            spin_kit4.setVisibility(View.GONE);

                            // progress.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //     progress.dismiss();
                spin_kit4.setVisibility(View.GONE);

                Toast.makeText(getContext(), "Check Internet Connection" + error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        rq.add(sr);

    }

    private void initScrollListener() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //super.onScrollStateChanged(recyclerView, newState);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();

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
                //  Toast.makeText(getContext(), "Page1:"+currentPage, Toast.LENGTH_SHORT).show();
                populateData(currentPage);
                initScrollListener();

                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 1000);


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