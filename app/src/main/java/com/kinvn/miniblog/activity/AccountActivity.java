package com.kinvn.miniblog.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kinvn.miniblog.Constants;
import com.kinvn.miniblog.R;
import com.kinvn.miniblog.base.BaseActivity;
import com.kinvn.miniblog.model.WeiboUserInfo;
import com.kinvn.miniblog.network.ApiManager;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Kinvn on 2017/9/1.
 */

public class AccountActivity extends BaseActivity {
    @BindView(R.id.account_list)
    RecyclerView listRv;

    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            Bundle bundle = data.getExtras();
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String latestUid = PreferenceManager.getDefaultSharedPreferences(this).getString("latest_uid", "");
        if (!TextUtils.isEmpty(latestUid)) {
            Observer<WeiboUserInfo> observer = new Observer<WeiboUserInfo>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(WeiboUserInfo weiboUserInfo) {
                    Log.i("tag1", weiboUserInfo.getName());
                }
            };
            SharedPreferences pref = getSharedPreferences(latestUid, MODE_APPEND);
            ApiManager.getInstance().getWbInfoApi()
                    .getWbUserInfo(pref.getString(Constants.KEY_ACCESS_TOKEN, ""), latestUid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    private void initViews() {
        List<String> list = new ArrayList<>();
        list.add("null");
        AccountAdapter adapter = new AccountAdapter();
        adapter.setAccountList(list);
        listRv.setAdapter(adapter);
        listRv.setLayoutManager(new LinearLayoutManager(this));
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken != null) {
            Log.i("tag1", mAccessToken.getToken());
        }
    }

    class AccountAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<String> accountList;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from
                    (AccountActivity.this).inflate(R.layout.item_account, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.account.setText(accountList.get(position));
        }

        @Override
        public int getItemCount() {
            return accountList == null ? 0 : accountList.size();
        }

        void setAccountList(List<String> list) {
            accountList = list;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.account_text)
        TextView account;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSsoHandler = new SsoHandler(AccountActivity.this);
                    mSsoHandler.authorize(new MyWbAuthListener());
                }
            });
        }
    }
}
