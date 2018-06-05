package io.jachoteam.omurbek.saktan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class HotlineListViewAdapter extends ArrayAdapter<HotlineModel> {

    Context context;

    public HotlineListViewAdapter(Context context, int resourceId,
                                  List<HotlineModel> hotlineModels) {
        super(context, resourceId, hotlineModels);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        HotlineListViewAdapter.ViewHolder holder = null;
        HotlineModel hotlineModel = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.hotline_list, null);
            holder = new HotlineListViewAdapter.ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.contactName = (TextView) convertView.findViewById(R.id.myname);
            holder.phone = (TextView) convertView.findViewById(R.id.phone);
            convertView.setTag(holder);
        } else
            holder = (HotlineListViewAdapter.ViewHolder) convertView.getTag();

        holder.contactName.setText(hotlineModel.getName());
        holder.phone.setText(hotlineModel.getPhone());
        if (hotlineModel.getName().equals("Пожарная служба")) {
            holder.imageView.setImageResource(R.mipmap.fire_icon_layout);
        } else if (hotlineModel.getName().equals("Милиция")) {
            holder.imageView.setImageResource(R.mipmap.police_icon_layout);
        } else if (hotlineModel.getName().equals("Скорая помощь")) {
            holder.imageView.setImageResource(R.mipmap.ambulance_icon_layout);
        } else if (hotlineModel.getName().equals("МЧС")) {
            holder.imageView.setImageResource(R.mipmap.emergency_icon_layout);
        } else if (hotlineModel.getName().equals("Детская поддержка")) {
            holder.imageView.setImageResource(R.mipmap.child_icon_layout);
        } else if (hotlineModel.getName().equals("ГРС")) {
            holder.imageView.setImageResource(R.mipmap.state_registration_layout);
        } else if (hotlineModel.getName().equals("Социальный фонд")) {
            holder.imageView.setImageResource(R.mipmap.social_fund_layout);
        } else {
            holder.imageView.setImageResource(R.drawable.googleg_standard_color_18);
        }

        return convertView;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView contactName;
        TextView phone;
    }
}
