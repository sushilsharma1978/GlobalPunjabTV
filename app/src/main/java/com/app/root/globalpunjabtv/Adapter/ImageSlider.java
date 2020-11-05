package com.app.root.globalpunjabtv.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.root.globalpunjabtv.R;
import com.squareup.picasso.Picasso;


public class ImageSlider extends PagerAdapter {

    private String[]  IMAGES;
    private LayoutInflater inflater;
    private Context context;

     ImageView imageView;
    public ImageSlider(Context context, String[] IMAGES) {
        this.context = context;
        this.IMAGES = IMAGES;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.length;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = LayoutInflater.from(context).inflate(R.layout.sliding_images, view, false);

        assert imageLayout != null;
        imageView = (ImageView) imageLayout
                .findViewById(R.id.slider_image);

        Picasso.with(context).load(IMAGES[position]).into(imageView);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}