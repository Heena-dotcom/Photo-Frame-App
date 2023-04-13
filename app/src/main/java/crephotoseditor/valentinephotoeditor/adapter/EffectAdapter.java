package crephotoseditor.valentinephotoeditor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import crephotoseditor.valentinephotoeditor.R;
import crephotoseditor.valentinephotoeditor.Utils.Effects;
import crephotoseditor.valentinephotoeditor.act.EditImageActivity;
import crephotoseditor.valentinephotoeditor.model.FrameListModel;

import java.util.ArrayList;

public class EffectAdapter extends RecyclerView.Adapter<EffectAdapter.FrameListViewHolder> {

    private static final String TAG = "log";
    private LayoutInflater layoutInflater;
    private View view;
    private Context activity;
    private ArrayList<FrameListModel> effectList = new ArrayList<>();
    EditImageActivity.EffectInterface effectInterface;

    public EffectAdapter(Context activity, ArrayList<FrameListModel> effectList, EditImageActivity.EffectInterface effectInterface) {
        this.activity = activity;
        this.effectList = effectList;
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
                .load(effectList.get(position).getEffect_thumb())
                .placeholder(R.mipmap.loading)
                .into(holder.imgEffectThumb);
        if (position == 0) {
            Effects.applyEffectNone(holder.imgEffectThumb);
//            img_name.setText(name[0]);
        } else if (position == 1) {
            Effects.applyEffect1(holder.imgEffectThumb);
//            img_name.setText(name[1]);
        } else if (position == 2) {
            Effects.applyEffect2(holder.imgEffectThumb);
//            img_name.setText(name[2]);
        } else if (position == 3) {
            Effects.applyEffect4(holder.imgEffectThumb);
//            img_name.setText(name[3]);
        } else if (position == 4) {
            Effects.applyEffect5(holder.imgEffectThumb);
//            img_name.setText(name[4]);
        } else if (position == 5) {
            Effects.applyEffect6(holder.imgEffectThumb);
//            img_name.setText(name[5]);
        } else if (position == 6) {
            Effects.applyEffect7(holder.imgEffectThumb);
//            img_name.setText(name[6]);
        } else if (position == 7) {
            Effects.applyEffect9(holder.imgEffectThumb);
//            img_name.setText(name[7]);
        } else if (position == 8) {
            Effects.applyEffect12(holder.imgEffectThumb);
//            img_name.setText(name[8]);
        } else if (position == 9) {
            Effects.applyEffect14(holder.imgEffectThumb);
//            img_name.setText(name[9]);
        } else if (position == 10) {
            Effects.applyEffect15(holder.imgEffectThumb);
//            img_name.setText(name[10]);
        } else if (position == 11) {
            Effects.applyEffect16(holder.imgEffectThumb);
//            img_name.setText(name[11]);
        } else if (position == 12) {
            Effects.applyEffect17(holder.imgEffectThumb);
//            img_name.setText(name[12]);
        } else if (position == 13) {
            Effects.applyEffect18(holder.imgEffectThumb);
//            img_name.setText(name[13]);
        } else if (position == 14) {
            Effects.applyEffect19(holder.imgEffectThumb);
//            img_name.setText(name[14]);
        } else if (position == 15) {
            Effects.applyEffect20(holder.imgEffectThumb);
//            img_name.setText(name[15]);
        } else if (position == 16) {
            Effects.applyEffect21(holder.imgEffectThumb);
//            img_name.setText(name[16]);
        } else if (position == 17) {
            Effects.applyEffect22(holder.imgEffectThumb);
//            img_name.setText(name[17]);
        }
    }

    @Override
    public int getItemCount() {
        return effectList.size();
    }

    class FrameListViewHolder extends RecyclerView.ViewHolder {

        ImageView imgEffectThumb;

        public FrameListViewHolder(View itemView) {
            super(itemView);
            imgEffectThumb = (ImageView) itemView.findViewById(R.id.imgEffectThumb);

            imgEffectThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    effectInterface.applyEffect(getAdapterPosition());
                }
            });
        }
    }
}
