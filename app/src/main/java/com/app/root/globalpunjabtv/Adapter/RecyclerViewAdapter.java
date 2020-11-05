package com.app.root.globalpunjabtv.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.root.globalpunjabtv.ActivitySinglePost;
import com.app.root.globalpunjabtv.R;
import com.app.root.globalpunjabtv.models.PostGetSet;
import com.github.ybq.android.spinkit.SpinKitView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable{

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<PostGetSet> mItemList;
    public List<PostGetSet> contactListFiltered;

Context context;
    public RecyclerViewAdapter(Context context, List<PostGetSet> itemList) {

        mItemList = itemList;
        this.context = context;
      contactListFiltered = itemList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items,
                    parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,
                    parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return contactListFiltered == null ? 0 : contactListFiltered.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return contactListFiltered.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = mItemList;
                } else {
                    List<PostGetSet> filteredList = new ArrayList<>();
                    for (PostGetSet row : mItemList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getPostTitle().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getPostTitle().toLowerCase().contains(charString.toUpperCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<PostGetSet>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_author;
        TextView tv_date;
        CardView card_view;
        ImageView tv_thumbnail;

        public ItemViewHolder( View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.title);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            tv_author=(TextView)itemView.findViewById(R.id.author);
            tv_date=(TextView)itemView.findViewById(R.id.date);
            tv_thumbnail=(ImageView)itemView.findViewById(R.id.thumbnail);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        SpinKitView progressBar;

        public LoadingViewHolder( View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
  viewHolder.progressBar.setVisibility(View.VISIBLE);

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        final PostGetSet postgetset = contactListFiltered.get(position);
        viewHolder.tv_title.setText(postgetset.getPostTitle());
        viewHolder.tv_author.setText(postgetset.getAuthorName());

        String newdate=parseDateToddMMyyyy(postgetset.getDate());

        viewHolder.tv_date.setText(newdate);
        Picasso.with(context)
                .load(postgetset.getThumbnail())
                .into(viewHolder.tv_thumbnail);



        final Integer postid=postgetset.getPostid();
        final String categoryname=postgetset.getCatname();
        final String posttitle=postgetset.getPostTitle();
        viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

          /*   sd.send(position,postid);

                SingleDetail singleDetailfragment=new SingleDetail();
                Bundle bundle=new Bundle();
                bundle.putInt("POSTID",postid);
                singleDetailfragment.setArguments(bundle);*/

              /* // Toast.makeText(context, "jhjhj", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("postid", postid);
                bundle.putInt("POSTID",id);
                context.startActivity(intent);
                Log.d("abc",arrayList.toString());*/
                Intent intent = new Intent(context, ActivitySinglePost.class);
                intent.putExtra("postid", postid);
                intent.putExtra("singlecatname", categoryname);
                intent.putExtra("post_titlename", posttitle);
              //  Toast.makeText(context, "Adapter"+categoryname, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);

            }
        });


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