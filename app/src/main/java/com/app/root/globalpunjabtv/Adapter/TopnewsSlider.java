package com.app.root.globalpunjabtv.Adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.root.globalpunjabtv.ActivitySinglePost;
import com.app.root.globalpunjabtv.R;
import com.app.root.globalpunjabtv.models.PostGetSet;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopnewsSlider extends PagerAdapter {

    private List<PostGetSet> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    public TopnewsSlider(Context context, List<PostGetSet> IMAGES) {
        this.context = context;
        this.IMAGES = IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.top_news_slider, view, false);

        assert imageLayout != null;
        final ImageView top_slider_image = (ImageView) imageLayout
                .findViewById(R.id.top_slider_image);
        TextView tv_text=imageLayout.findViewById(R.id.tv_text);
        LinearLayout ll_singlenews=imageLayout.findViewById(R.id.ll_singlenews);

        ll_singlenews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivitySinglePost.class);
                intent.putExtra("postid", IMAGES.get(position).getPostid());
                intent.putExtra("singlecatname", IMAGES.get(position).getCatname());
                intent.putExtra("post_titlename", IMAGES.get(position).getPostTitle());
                //  Toast.makeText(context, "Adapter"+categoryname, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });

        Picasso.with(context).load(IMAGES.get(position).getThumbnail()).into(top_slider_image);
        tv_text.setText(IMAGES.get(position).getPostTitle());

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}