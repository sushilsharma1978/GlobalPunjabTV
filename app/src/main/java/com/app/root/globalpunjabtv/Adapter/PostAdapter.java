package com.app.root.globalpunjabtv.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.root.globalpunjabtv.R;
import com.app.root.globalpunjabtv.SinglePostnavigationActivity;
import com.app.root.globalpunjabtv.models.PostGetSet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>
{


    ArrayList<PostGetSet> arrayList=new ArrayList<>();
    Context context;
   // SendData sd;
    FragmentManager fragmentManager;
    public PostAdapter(Context context, ArrayList<PostGetSet> arrayList,
                       FragmentManager fragmentManager)
    {
        this.context=context;
        this.arrayList=arrayList;
        this.fragmentManager=fragmentManager;
       // this.sd=sd;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items, parent, false);
        MyViewHolder vh = new PostAdapter.MyViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PostGetSet postgetset = arrayList.get(position);
        holder.tv_title.setText(postgetset.getPostTitle());
        holder.tv_author.setText(postgetset.getAuthorName());
        holder.tv_date.setText(postgetset.getDate());
        Picasso.with(context)
                .load(postgetset.getThumbnail())
                .into(holder.tv_thumbnail);



        final Integer postid=postgetset.getPostid();
        holder.card_view.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(context, SinglePostnavigationActivity.class);
                intent.putExtra("postid", postid);
               // intent.putExtra("postid", postid);
                context.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_title;
        TextView tv_author;
        TextView tv_date;
        CardView card_view;
        ImageView tv_thumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.title);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            tv_author=(TextView)itemView.findViewById(R.id.author);
            tv_date=(TextView)itemView.findViewById(R.id.date);
            tv_thumbnail=(ImageView)itemView.findViewById(R.id.thumbnail);
        }
    }
}
