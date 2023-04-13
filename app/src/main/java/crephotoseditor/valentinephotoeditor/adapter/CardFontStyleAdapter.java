package crephotoseditor.valentinephotoeditor.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import crephotoseditor.valentinephotoeditor.R;


public class CardFontStyleAdapter extends BaseAdapter {
    private Context context;
    private final String[] mobileValues;

    static class RecordHolder {
        TextView txt_font;
        Typeface type;
    }

    public CardFontStyleAdapter(Context context, String[] mobileValues) {
        this.context = context;
        this.mobileValues = mobileValues;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        RecordHolder holder;
        View row = convertView;
        if (row == null) {
            row = ((Activity) this.context).getLayoutInflater().inflate(R.layout.listitem_fontstyle, parent, false);
            holder = new RecordHolder();
            holder.txt_font = (TextView) row.findViewById(R.id.img_grid_item);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        holder.type = Typeface.createFromAsset(this.context.getAssets(), this.mobileValues[position]);
        holder.txt_font.setTypeface(holder.type);
        return row;
    }

    public int getCount() {
        return this.mobileValues.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
}
