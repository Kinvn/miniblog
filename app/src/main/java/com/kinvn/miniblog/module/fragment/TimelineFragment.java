package com.kinvn.miniblog.module.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kinvn.miniblog.R;
import com.kinvn.miniblog.base.BaseFragment;
import com.kinvn.miniblog.model.StatusesResult;
import com.kinvn.miniblog.model.WbStatus;
import com.kinvn.miniblog.model.manager.OauthManager;
import com.kinvn.miniblog.network.ApiManager;
import com.kinvn.miniblog.utils.TimeUtils;

import java.text.ParseException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Kinvn on 2017/8/28.
 */

public class TimelineFragment extends BaseFragment {
    @BindView(R.id.weibo_content)
    RecyclerView wbContentList;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    private List<WbStatus> statuses;
    private WbContentAdapter mAdapter;

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
        mAdapter = new WbContentAdapter();
        wbContentList.setAdapter(mAdapter);
        wbContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        wbContentList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onResume() {
        super.onResume();
        mRefreshLayout.setRefreshing(true);
        getTimelineStatuses();
    }

    private void getTimelineStatuses() {
        ApiManager.getInstance().getWbStatusesApi()
                .getHomeTimelineStatuses(OauthManager.getInstance().getToken().getToken(), 0, 100, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<StatusesResult>() {
                    @Override
                    public void call(StatusesResult statusesResult) {
                        statuses = statusesResult.getStatuses();
                        mAdapter.notifyDataSetChanged();
                        mRefreshLayout.setRefreshing(false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    class WbContentAdapter extends RecyclerView.Adapter<WbContentAdapter.MyViewHolder> {

        WbContentAdapter() {

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from
                    (getContext()).inflate(R.layout.item_weibo_content, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.statusAuthor.setText(statuses.get(position).getUser().getName());
            holder.statusText.setText(statuses.get(position).getText());
            try {
                holder.statusTime.setText(TimeUtils.getTimeByWbApiTime
                        (statuses.get(position).getCreated_at(), statuses.get(position).getSource()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Glide.with(getContext()).load(statuses.get(position).getUser().getProfile_image_url()).into(holder.statusIcon);
        }

        @Override
        public int getItemCount() {
            return statuses == null ? 0 : statuses.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.status_author)
            TextView statusAuthor;
            @BindView(R.id.status_text)
            TextView statusText;
            @BindView(R.id.status_icon)
            ImageView statusIcon;
            @BindView(R.id.status_time)
            TextView statusTime;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
