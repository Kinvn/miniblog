package com.kinvn.miniblog.module.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kinvn.miniblog.R;
import com.kinvn.miniblog.base.BaseFragment;
import com.kinvn.miniblog.model.StatusesResult;
import com.kinvn.miniblog.model.WbStatus;
import com.kinvn.miniblog.model.manager.OauthManager;
import com.kinvn.miniblog.network.ApiManager;
import com.kinvn.miniblog.utils.TimeUtils;
import com.kinvn.miniblog.view.adapter.StatusPicsAdapter;

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
    private static final int DEFAULT_STATUSES_COUNT = 50;

    @BindView(R.id.weibo_content)
    RecyclerView wbContentList;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    private List<WbStatus> statuses;
    private WbContentAdapter mAdapter;
    private int page = 1;

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
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getTimelineStatuses(page);
            }
        });
        mAdapter = new WbContentAdapter();
        wbContentList.setAdapter(mAdapter);
        wbContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        wbContentList.setItemAnimator(new DefaultItemAnimator());
        wbContentList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING
                        && lastVisibleItemPosition == totalItemCount - 1
                        && visibleItemCount > 0) {
                    Toast.makeText(getContext(), "加载中", Toast.LENGTH_LONG).show();
                    getTimelineStatuses(page++);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (statuses == null || statuses.size() == 0)
            getTimelineStatuses(page);
    }

    private void getTimelineStatuses(final int page) {
        ApiManager.getInstance().getWbStatusesApi()
                .getHomeTimelineStatuses(OauthManager.getInstance().getToken().getToken(), 0,
                        DEFAULT_STATUSES_COUNT, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<StatusesResult>() {
                    @Override
                    public void call(StatusesResult statusesResult) {
                        if (page == 1 || statuses == null)
                            statuses = statusesResult.getStatuses();
                        else {
                            statuses.addAll(statusesResult.getStatuses());
                        }
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
            WbStatus status = statuses.get(position);
            holder.statusAuthor.setText(status.getUser().getName());
            holder.statusText.setText(status.getText());
            try {
                holder.statusTime.setText(TimeUtils.getTimeByWbApiTime
                        (status.getCreated_at(), status.getSource()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Glide.with(getContext()).load(status.getUser().getProfile_image_url()).into(holder.statusIcon);
            holder.statusContent.removeAllViews();
            if (status.getRetweeted_status() == null) {
                List<WbStatus.WbPicture> picUrls = status.getPic_urls();
                if (picUrls != null && picUrls.size() > 0) {
                    RecyclerView recyclerView = new RecyclerView(getContext());
                    recyclerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                            , ViewGroup.LayoutParams.WRAP_CONTENT));
                    StatusPicsAdapter adapter = new StatusPicsAdapter(picUrls, getContext());
                    recyclerView.setAdapter(adapter);
                    int i;
                    if (picUrls.size() >= 3)
                        i = 3;
                    else
                        i = 2;
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), i));
                    holder.statusContent.addView(recyclerView);
                }
            }
            holder.repostsCount.setText(String.valueOf(status.getReposts_count()));
            holder.comments_count.setText(String.valueOf(status.getComments_count()));
            holder.attitudes_count.setText(String.valueOf(status.getAttitudes_count()));
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
            @BindView(R.id.status_content)
            FrameLayout statusContent;
            @BindView(R.id.reposts_count)
            TextView repostsCount;
            @BindView(R.id.comments_count)
            TextView comments_count;
            @BindView(R.id.attitudes_count)
            TextView attitudes_count;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
