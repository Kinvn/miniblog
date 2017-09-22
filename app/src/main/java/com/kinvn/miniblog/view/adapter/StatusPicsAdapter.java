package com.kinvn.miniblog.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kinvn.miniblog.model.WbStatus;
import com.kinvn.miniblog.view.SquareImageView;

import java.util.List;

/**
 * Created by Kinvn on 2017/9/19.
 */

public class StatusPicsAdapter extends RecyclerView.Adapter<StatusPicsAdapter.PicsViewHolder> {
    private List<WbStatus.WbPicture> picsUrls;
    private Context mContext;

    public StatusPicsAdapter(List<WbStatus.WbPicture> urls, Context context) {
        picsUrls = urls;
        mContext = context;
    }

    @Override
    public PicsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PicsViewHolder(new SquareImageView(mContext));
    }

    @Override
    public void onBindViewHolder(PicsViewHolder holder, int position) {
        Glide.with(mContext)
                .load(picsUrls.get(position).getThumbnail_pic().replace("thumbnail", "large"))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into((ImageView) holder.itemView);
    }

    @Override
    public int getItemCount() {
        return picsUrls == null ? 0 : picsUrls.size();
    }

    class PicsViewHolder extends RecyclerView.ViewHolder {
        PicsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
