package com.kinvn.miniblog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kinvn.miniblog.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kinvn on 2017/8/28.
 */

public class TimelineFragment extends Fragment{
    @BindView(R.id.weibo_content)
    RecyclerView weiboContentList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timeline, container, false);
        ButterKnife.bind(this, v);
        initViews();
        return v;
    }

    private void initViews() {
        List<String> list = new ArrayList<>();
        for (int i = 'a'; i < 'z'; i++) {
            list.add("" + (char) i);
        }
        weiboContentList.setAdapter(new WeiboContentAdapter(list));
        weiboContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        weiboContentList.setItemAnimator(new DefaultItemAnimator());
    }

    class WeiboContentAdapter extends RecyclerView.Adapter<WeiboContentAdapter.MyViewHolder> {
        List<String> mList;

        WeiboContentAdapter(List<String> list) {
            mList = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from
                    (getContext()).inflate(R.layout.item_weibo_content, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_text_view)
            TextView tv;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
