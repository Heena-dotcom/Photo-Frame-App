package crephotoseditor.valentinephotoeditor.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import crephotoseditor.valentinephotoeditor.R;
import crephotoseditor.valentinephotoeditor.model.FrameListModel;

import java.util.ArrayList;


public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.FrameListViewHolder> {

    private static final String TAG = "log";
    private int width, height;
    private LayoutInflater layoutInflater;
    private View view;
    private Activity activity;
    private ArrayList<FrameListModel> frameList = new ArrayList<>();
    clickFrame clickFrame;
    public FrameAdapter(Activity activity, ArrayList<FrameListModel> frameList,clickFrame clickFrame) {
        this.activity = activity;
        this.frameList = frameList;
        this.layoutInflater = LayoutInflater.from(activity);
        this.clickFrame=clickFrame;

    }


    @Override
    public FrameListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.frame_item, parent, false);
        return new FrameListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FrameListViewHolder holder, int position) {
        Glide.with(activity)
                .load(frameList.get(position).getThumb())
//                .override(width / 2, Target.SIZE_ORIGINAL)
                .into(holder.imgFrame);

//        holder.imgFrame.setPadding(3, 3, 3, 3);
    }

    @Override
    public int getItemCount() {
        return frameList.size();
    }

    class FrameListViewHolder extends RecyclerView.ViewHolder {

        ImageView imgFrame;


        public FrameListViewHolder(View itemView) {
            super(itemView);
            imgFrame = (ImageView) itemView.findViewById(R.id.imgFrame);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            height = displaymetrics.heightPixels;
            width = displaymetrics.widthPixels;


            imgFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent toEdit = new Intent(activity, EditImageActivity.class);
//                    toEdit.putExtra("frame", frameList.get(getAdapterPosition()).getFrame());
//                    activity.startActivityForResult(toEdit, REQ_EDITIMAGE);
//                    activity.overridePendingTransition(R.anim.enter_transition, 0);
clickFrame.clickedFrame(frameList.get(getAdapterPosition()).getFrame());
                }
            });
        }
    }
   public interface clickFrame{
        public void clickedFrame(int pos);
    }
}
