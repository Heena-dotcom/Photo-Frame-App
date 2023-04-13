package crephotoseditor.valentinephotoeditor.adapter;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import crephotoseditor.valentinephotoeditor.BuildConfig;
import crephotoseditor.valentinephotoeditor.R;


public class MyCreationAdapter extends RecyclerView.Adapter<MyCreationAdapter.ViewHolder> {
    ArrayList<String> filePath;
    Activity activity;
    RecyclerViewClickListener recyclerViewClickListener;
    checkSize checksize;
    int height, width;

    public MyCreationAdapter(ArrayList<String> filePath, Activity activity, RecyclerViewClickListener recyclerViewClickListener, checkSize checksize) {
        this.filePath = filePath;
        this.activity = activity;
        this.recyclerViewClickListener = recyclerViewClickListener;
        this.checksize = checksize;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        View viewItem = LayoutInflater.from(activity).inflate(R.layout.item_my_creation, parent, false);
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(activity)
                .load(Uri.fromFile(new File(filePath.get(position))))
                .placeholder(R.mipmap.loading)
                .into(holder.imgIcon);

        String name = new File(filePath.get(position)).getName();
        holder.txtName.setText(name);

//        holder.cardView.setLayoutParams(new LinearLayout.LayoutParams(width / 2, (width / 2) + 80));
    }

    @Override
    public int getItemCount() {
        return filePath.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgIcon, imgDelete, imgShare, imgWallpaper;
        TextView txtName;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
            imgShare = (ImageView) itemView.findViewById(R.id.imgShare);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            imgWallpaper = (ImageView) itemView.findViewById(R.id.imgWallpaper);

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Are you sure to delete this?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File f = new File(filePath.get(getAdapterPosition()));
                            if (f.exists()) {
                                f.delete();
                                filePath.remove(getAdapterPosition());
                                notifyDataSetChanged();
                                if (filePath.size() == 0) {
                                    checksize.sendSize(filePath.size());
                                }
                            }
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                }
            });

            imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    Uri photoURI = FileProvider.getUriForFile(activity,
                            BuildConfig.APPLICATION_ID + ".provider",
                            new File(filePath.get(getAdapterPosition())));
                    i.setType("image/*");
                    i.putExtra(Intent.EXTRA_TEXT,  activity.getResources().getString(R.string.app_name) + " https://play.google.com/store/apps/details?id=" +activity. getPackageName());
                    i.putExtra(Intent.EXTRA_STREAM, photoURI);
                    activity.startActivity(i);
                }
            });
            imgWallpaper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Retrieve a WallpaperManager
                    WallpaperManager myWallpaperManager = WallpaperManager
                            .getInstance(activity);

                    try {
                        // Change the current system wallpaper
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath.get(getAdapterPosition()), bmOptions);
                        myWallpaperManager.setBitmap(bitmap);

                        // Show a toast message on successful change
                        Toast.makeText(activity,
                                "Wallpaper successfully changed", Toast.LENGTH_SHORT)
                                .show();

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                    }
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewClickListener.recyclerclicked(v, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {
        void recyclerclicked(View v, int position);
    }

    public interface checkSize {
        void sendSize(int size);
    }
}
