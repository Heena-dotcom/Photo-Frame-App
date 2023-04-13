package crephotoseditor.valentinephotoeditor.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import crephotoseditor.valentinephotoeditor.R;

public class ExitAppListAdapter extends BaseAdapter {

    private Activity dactivity;
    private static LayoutInflater inflater = null;
    SparseBooleanArray mSparseBooleanArray;
    ArrayList<String> dLink = new ArrayList<String>();
    ArrayList<String> dName = new ArrayList<String>();
    ArrayList<String> dIcon = new ArrayList<String>();

    MediaMetadataRetriever metaRetriever;
    View vi;
    private int imageSize;
    int width;
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();

    public ExitAppListAdapter(Activity dAct, ArrayList<String> dLinkUrl, ArrayList<String> dIcon, ArrayList<String> dName) {
        dactivity = dAct;
        this.dLink = dLinkUrl;
        this.dName = dName;
        this.dIcon = dIcon;

        inflater = (LayoutInflater) dactivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSparseBooleanArray = new SparseBooleanArray(dLink.size());
    }

    public int getCount() {
        return dLink.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;
        DisplayMetrics metrics = dactivity.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        int height = metrics.heightPixels;

        if (row == null) {

            row = LayoutInflater.from(dactivity).inflate(R.layout.item_app_ad, parent, false);
            holder = new ViewHolder();

            holder.ivAppIcon = (ImageView) row.findViewById(R.id.ivAppicon);
            holder.ivAppname = (TextView) row.findViewById(R.id.txtAppname);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.ivAppname.setText(dName.get(position));

        Glide.with(dactivity)
                .load(dIcon.get(position))
                .centerCrop()
                .placeholder(R.mipmap.appicon)
                .crossFade()
                .into(holder.ivAppIcon);

        System.gc();
        return row;

    }

    static class ViewHolder {

        ImageView ivAppIcon;
        TextView ivAppname;

    }
}
