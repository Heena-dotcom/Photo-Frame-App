package crephotoseditor.valentinephotoeditor.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import crephotoseditor.valentinephotoeditor.R;


public class AppAdsAdapter extends RecyclerView.Adapter<AppAdsAdapter.ViewHolder> {

    Context context;
    ArrayList<String> listAppLink = new ArrayList<String>();
    ArrayList<String> listAppIcon = new ArrayList<String>();
    ArrayList<String> listAppName = new ArrayList<String>();

    public AppAdsAdapter(Context context, ArrayList<String> listAppLink, ArrayList<String> listAppIcon, ArrayList<String> listAppName) {
        this.context = context;
        this.listAppLink = listAppLink;
        this.listAppIcon = listAppIcon;
        this.listAppName = listAppName;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app_ad, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.txtAppname.setText(listAppName.get(position));

        Glide.with(context)
                .load(listAppIcon.get(position))
                .placeholder(R.mipmap.appicon)
                .crossFade()
                .into(holder.ivAppicon);

    }

    @Override
    public int getItemCount() {
        return listAppLink.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView card_view;
        ImageView ivAppicon;
        TextView txtAppname;

        public ViewHolder(View itemView) {
            super(itemView);

            card_view = (CardView) itemView.findViewById(R.id.card_view);
            ivAppicon = (ImageView) itemView.findViewById(R.id.ivAppicon);
            txtAppname = (TextView) itemView.findViewById(R.id.txtAppname);

            card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(listAppLink.get(getAdapterPosition()));
                    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        context.startActivity(myAppLinkToMarket);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

}
