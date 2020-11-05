package com.app.root.globalpunjabtv.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.app.root.globalpunjabtv.R;
import com.app.root.globalpunjabtv.models.CatGetSet;

import java.util.ArrayList;

public class CatAdapter extends BaseAdapter {
    ArrayList<CatGetSet> arrayList2=new ArrayList<>();
    private Context context;
    public CatAdapter(Context context,ArrayList arrayList2)
    {
        this.context=context;
        this.arrayList2=arrayList2;
    }

    @Override
    public int getCount() {
        return arrayList2.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_items, null, true);

            holder.catname = (TextView) convertView.findViewById(R.id.catname);
            final CatGetSet catgetset = arrayList2.get(position);
            holder.catname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String catid=catgetset.getCatid();
                    String catname=catgetset.getCatname();
                    String catlink=catgetset.getCatlink();

                }
            });

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.catname.setText(arrayList2.get(position).getCatname());

        return convertView;
    }
    private class ViewHolder {

        protected TextView catname;
    }
}
