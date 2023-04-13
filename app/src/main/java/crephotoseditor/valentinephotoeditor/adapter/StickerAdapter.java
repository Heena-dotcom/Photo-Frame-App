package crephotoseditor.valentinephotoeditor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import crephotoseditor.valentinephotoeditor.R;
import crephotoseditor.valentinephotoeditor.act.EditImageActivity;
import crephotoseditor.valentinephotoeditor.model.FrameListModel;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.FrameListViewHolder> {

    private static final String TAG = "log";
    private LayoutInflater layoutInflater;
    private View view;
    private Context activity;
    private ArrayList<FrameListModel> stickerList = new ArrayList<>();
    private EditImageActivity.EffectInterface effectInterface;

    public StickerAdapter(Context activity, ArrayList<FrameListModel> stickerList, EditImageActivity.EffectInterface effectInterface) {
        this.activity = activity;
        this.stickerList = stickerList;
        this.effectInterface = effectInterface;
        this.layoutInflater = LayoutInflater.from(activity);
    }


    @Override
    public FrameListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.effect_item, parent, false);
        return new FrameListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FrameListViewHolder holder, int position) {
        Glide.with(activity)
                .load(stickerList.get(position).getEffect_thumb())
                .placeholder(R.mipmap.loading)
                .into(holder.imgEffectThumb);
    }

    @Override
    public int getItemCount() {
        return stickerList.size();
    }

    class FrameListViewHolder extends RecyclerView.ViewHolder {

        ImageView imgEffectThumb;

        public FrameListViewHolder(View itemView) {
            super(itemView);
            imgEffectThumb = (ImageView) itemView.findViewById(R.id.imgEffectThumb);

            imgEffectThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    effectInterface.addSticker(getAdapterPosition());
                }
            });
        }
    }
}
