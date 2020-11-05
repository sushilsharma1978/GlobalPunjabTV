package com.app.root.globalpunjabtv.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.root.globalpunjabtv.R;
import com.app.root.globalpunjabtv.inteface.RelatednewsSend;
import com.app.root.globalpunjabtv.models.PostGetSet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RelatedNewsAdapter extends RecyclerView.Adapter<RelatedNewsAdapter.ViewHolder> {
    Context context;
    List<PostGetSet> list2;
    RelatednewsSend relatednewsSend;

    public RelatedNewsAdapter(Context context, List<PostGetSet> list2, RelatednewsSend relatednewsSend) {
        this.context = context;
        this.list2 = list2;
        this.relatednewsSend = relatednewsSend;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_deatil_photos,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final PostGetSet postgetset = list2.get(i);
        viewHolder.tv_title.setText(postgetset.getPostTitle());
        viewHolder.tv_author.setText(postgetset.getAuthorName());


        String newdate=parseDateToddMMyyyy(postgetset.getDate());

        viewHolder.tv_date.setText(newdate);

        Picasso.with(context)
                .load(postgetset.getThumbnail())
                .into(viewHolder.tv_thumbnail);

        viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relatednewsSend.newssend(postgetset.getPostid(), postgetset.getCatname(),
                        postgetset.getPostTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list2.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_author;
        TextView tv_date;
        CardView card_view;
        ImageView tv_thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.title1);
            card_view = (CardView) itemView.findViewById(R.id.card_view1);
            tv_author = (TextView) itemView.findViewById(R.id.author1);
            tv_date = (TextView) itemView.findViewById(R.id.date1);
            tv_thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail1);
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
