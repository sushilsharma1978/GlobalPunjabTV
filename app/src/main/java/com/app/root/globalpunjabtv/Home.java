package com.app.root.globalpunjabtv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.root.globalpunjabtv.inteface.Selecttab;
import com.app.root.globalpunjabtv.inteface.SendSearchData;
import com.app.root.globalpunjabtv.models.CatGetSet;
import com.app.root.globalpunjabtv.models.PostGetSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Selecttab {
    String caturl;
    List<CatGetSet> catGetSetList = new ArrayList<>();
    NavigationView navigationView;
    ViewPager viewPager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String lang;
    int selected_itemid;
    TabLayout tabLayout;
    Intent intentt;
    BlankFragment blankFragment;
    CategoryFragment categoryFragment;
    ImageView iv_livetv;
    static ViewPagerAdapter pagerAdapter;
    SearchView searchView;
    List<PostGetSet> sendsearchlist=new ArrayList<>();
    SendSearchData sendSearchData;
    // static MyPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //sendSearchData=(SendSearchData) this;

        sharedPreferences = getSharedPreferences("Categories", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        lang = sharedPreferences.getString("language", "");
        new CatData().execute();

        viewPager = (ViewPager) findViewById(R.id.pager);
        iv_livetv = findViewById(R.id.iv_livetv);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);


        iv_livetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, LiveTvActivity.class));
            }
        });

    }

    @Override
    public void select(String id, String categoryname, int position) {
        // Toast.makeText(this, "ID:" + id, Toast.LENGTH_SHORT).show();

        editor.putString("categoryid", id);
        editor.putString("categoryname", categoryname);

        editor.commit();

    }



    public class CatData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (lang.equals("punjabi")) {
                caturl = "http://globalpunjabtv.com/api/cat_by_order.php";
                Log.e("enter punjabi", caturl);
            } else {
                caturl = "http://globalenglishnews.com/cat_by_order.php";
            }
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

                   /* if (lang.equals("punjabi")) {
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
/*
                        }
                    }*/


                }

               /* CatGetSet catGetSett2 = new CatGetSet("100",
                        "", "Live Tv");
                catGetSetList.add(catGetSett2);*/


            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(Home.this, "Something went wrong", Toast.LENGTH_SHORT).show();


            }

            addMenuItemInNavMenuDrawer(catGetSetList);

            pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                    Home.this, catGetSetList, Home.this);
            viewPager.setAdapter(pagerAdapter);

            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                tab.setCustomView(pagerAdapter.getTabView(i));
            }

            intentt = getIntent();
            if (intentt.hasExtra("selected_item")) {
                selected_itemid = intentt.getIntExtra("selected_item", 1000);
                TabLayout.Tab tab = tabLayout.getTabAt(selected_itemid);
                tab.select();
                //  Toast.makeText(Home.this, ""+selected_itemid, Toast.LENGTH_SHORT).show();


            } else {
                // Log.d("msg", "onCreatecheck: hjhjhjhjk");
            }


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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int orderid = item.getOrder();
        if (item.getTitle().equals("All")) {

            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();

            editor.putString("categoryid", String.valueOf(item.getItemId()));
            editor.putString("categoryname", String.valueOf(item.getTitle()));
            Log.d("msg", "populateDataNav: " + item.getTitle() + "," + sharedPreferences.getString("categoryname", ""));

            editor.commit();
            blankFragment = new BlankFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_all, blankFragment).addToBackStack(null)
                    .commit();

        } /*else if (item.getTitle().equals("Live Tv")) {
            TabLayout.Tab tab = tabLayout.getTabAt(orderid);
            tab.select();

            editor.putString("categoryid", String.valueOf(item.getItemId()));
            editor.putString("categoryname", String.valueOf(item.getTitle()));
            Log.d("msg", "populateDataNav: " + item.getTitle() + "," + sharedPreferences.getString("categoryname", ""));

            editor.commit();
           *//* LiveTVFragment liveTVFragment = new LiveTVFragment();
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.layout_frame, liveTVFragment).addToBackStack(null).commit();*//*

            startActivity(new Intent(Home.this, LiveTvActivity.class));
        }*/ else {
            TabLayout.Tab tab = tabLayout.getTabAt(orderid);
            tab.select();

            editor.putString("categoryid", String.valueOf(item.getItemId()));
            editor.putString("categoryname", String.valueOf(item.getTitle()));
            Log.d("msg", "populateDataNav: " + item.getTitle() + "," + sharedPreferences.getString("categoryname", ""));

            editor.commit();
            //Toast.makeText(this, ""+sharedPreferences.getString("categoryid",""), Toast.LENGTH_SHORT).show();
            categoryFragment = new CategoryFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_category, categoryFragment).addToBackStack(null)
                    .commit();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        Context context;
        List<CatGetSet> catGetSetList;
        Selecttab st;

        public ViewPagerAdapter(FragmentManager fm, Context context, List<CatGetSet> catGetSetList,
                                Selecttab st) {
            super(fm);
            this.context = context;
            this.catGetSetList = catGetSetList;
            this.st = st;
        }

        @Override
        public int getCount() {
            return catGetSetList.size();
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                st.select(catGetSetList.get(position).getCatid(),
                        catGetSetList.get(position).getCatname(),
                        position);
                //Toast.makeText(context,"0"+position,Toast.LENGTH_LONG).show();
                blankFragment = new BlankFragment();
                return blankFragment;
            }
            if (position == 1) {
                st.select(catGetSetList.get(position).getCatid(),
                        catGetSetList.get(position).getCatname(),
                        position);
                // Toast.makeText(context,"1"+position,Toast.LENGTH_LONG).show();

                categoryFragment = new CategoryFragment();
                return categoryFragment;
            }
            if (position == 2) {
                st.select(catGetSetList.get(position).getCatid(),
                        catGetSetList.get(position).getCatname(),
                        position);
                //  Toast.makeText(context,"2"+position,Toast.LENGTH_LONG).show();

//                    Toast.makeText(context,"cat",Toast.LENGTH_LONG).show();
                categoryFragment = new CategoryFragment();
                return categoryFragment;
            }

            if (position == 3) {
                st.select(catGetSetList.get(position).getCatid(),
                        catGetSetList.get(position).getCatname(),
                        position);
                //  Toast.makeText(context,"3"+position,Toast.LENGTH_LONG).show();

//                    Toast.makeText(context,"cat",Toast.LENGTH_LONG).show();
                categoryFragment = new CategoryFragment();
                return categoryFragment;
            }

            if (position == 4) {
                st.select(catGetSetList.get(position).getCatid(),
                        catGetSetList.get(position).getCatname(),
                        position);
                //Toast.makeText(context,"4"+position,Toast.LENGTH_LONG).show();

//                    Toast.makeText(context,"cat",Toast.LENGTH_LONG).show();
                categoryFragment = new CategoryFragment();
                return categoryFragment;
            }

            if (position == 5) {
                st.select(catGetSetList.get(position).getCatid(),
                        catGetSetList.get(position).getCatname(),
                        position);

                // Toast.makeText(context,"5"+position,Toast.LENGTH_LONG).show();

//                    Toast.makeText(context,"cat",Toast.LENGTH_LONG).show();
                categoryFragment = new CategoryFragment();
                return categoryFragment;
            }

            if (position == 6) {
                st.select(catGetSetList.get(position).getCatid(),
                        catGetSetList.get(position).getCatname(),
                        position);
                //   Toast.makeText(context,"6"+position,Toast.LENGTH_LONG).show();

//                    Toast.makeText(context,"cat",Toast.LENGTH_LONG).show();
                categoryFragment = new CategoryFragment();
                return categoryFragment;
            }

            if (position == 7) {
                st.select(catGetSetList.get(position).getCatid(),
                        catGetSetList.get(position).getCatname(),
                        position);
                //  Toast.makeText(context,"7"+position,Toast.LENGTH_LONG).show();
//                    Toast.makeText(context,"cat",Toast.LENGTH_LONG).show();
                categoryFragment = new CategoryFragment();
                return categoryFragment;
            }
            if (position == 8) {
                st.select(catGetSetList.get(position).getCatid(),
                        catGetSetList.get(position).getCatname(),
                        position);
                //   Toast.makeText(context,"8"+position,Toast.LENGTH_LONG).show();
                categoryFragment = new CategoryFragment();
                return categoryFragment;
            } else {
                st.select(catGetSetList.get(position).getCatid(),
                        catGetSetList.get(position).getCatname(),
                        position);
                // Toast.makeText(context, "default:"+position, Toast.LENGTH_SHORT).show();
                categoryFragment = new CategoryFragment();
                return categoryFragment;
            }
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return catGetSetList.get(position).getCatname();
        }

        public View getTabView(int position) {
            View tab = LayoutInflater.from(Home.this).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) tab.findViewById(R.id.text);
            tv.setText(catGetSetList.get(position).getCatname());
            return tab;
        }

    }


  /*  class MyPagerAdapter extends FragmentPagerAdapter {

        List<CatGetSet> catGetSetList;
        Selecttab st;
Context context;
        public MyPagerAdapter(FragmentManager fm, Context context, List<CatGetSet> catGetSetList,
                            Selecttab st) {
            super(fm);
            this.context = context;
            this.catGetSetList = catGetSetList;
            this.st = st;
        }

        @Override
        public Fragment getItem(int position) {
            //return MyFragment.newInstance();

            return Fragment.instantiate(context, fragmentsA.get(position));

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return catGetSetList.get(position).getCatname();
        }

        @Override
        public int getCount() {
            // return CONTENT.length;
            return catGetSetList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        public View getTabView(int position) {
            View tab = LayoutInflater.from(Home.this).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) tab.findViewById(R.id.text);
            tv.setText(catGetSetList.get(position).getCatname());
            return tab;
        }
    }*/



   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.layout_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null) {
            searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    //some operation
                    return false;
                }


            });
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //some operation
                }
            });
            EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchPlate.setHint("Search");
            View searchPlateView = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
            searchPlateView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
            // use this method for search process
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // use this method when query submitted
                    sendSearchData.sendtoFragment(sendsearchlist);

                    //    Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // use this method for auto complete search process
                  //  Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_SHORT).show();

                    return false;
                }
            });
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        }
        return super.onCreateOptionsMenu(menu);
    }*/

}