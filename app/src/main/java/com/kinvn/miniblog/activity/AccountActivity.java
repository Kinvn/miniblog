package com.kinvn.miniblog.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kinvn.miniblog.R;
import com.kinvn.miniblog.base.BaseActivity;
import com.kinvn.miniblog.db.MiniBlogDB;
import com.kinvn.miniblog.model.WbUserInfo;
import com.kinvn.miniblog.model.manager.OauthManager;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kinvn on 2017/9/1.
 */

public class AccountActivity extends BaseActivity {
    @BindView(R.id.account_list)
    RecyclerView listRv;

    private SsoHandler mSsoHandler;
    private List<WbUserInfo> wbUserInfoList;
    private List<String> accountNameList;
    private List<Bitmap> iconList;
    private AccountAdapter accountAdapter = new AccountAdapter();

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
        init();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String latestUid = PreferenceManager.getDefaultSharedPreferences(this).getString("latest_uid", "");
    }

    private void init() {
        notifyData();
    }

    @Override
    protected void notifyData() {
        wbUserInfoList = MiniBlogDB.getInstance(this).loadAccountInfo();
        accountNameList = new ArrayList<>();
        iconList = new ArrayList<>();
        for (WbUserInfo w : wbUserInfoList) {
            accountNameList.add(w.getName());
            File icon = new File(getFilesDir().toString() + "/icon/" + String.valueOf(w.getId()) + ".png");
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(icon));
                iconList.add(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        accountNameList.add(getString(R.string.add_account));
        iconList.add(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_add));
        accountAdapter.notifyDataSetChanged();
    }

    private void initViews() {
        listRv.setAdapter(accountAdapter);
        listRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        listRv.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setTitle(R.string.account_management);
    }

    class AccountAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from
                    (AccountActivity.this).inflate(R.layout.item_account, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.account.setText(accountNameList.get(position));
            holder.icon.setImageBitmap(iconList.get(position));
        }

        @Override
        public int getItemCount() {
            return accountNameList == null ? 0 : accountNameList.size();
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.account_text)
        TextView account;
        @BindView(R.id.account_icon)
        ImageView icon;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() != accountNameList.size() -1) {
                        OauthManager.getInstance().setToken(MiniBlogDB.getInstance(AccountActivity.this)
                                .loadOauthInfo(String.valueOf(wbUserInfoList.get(getAdapterPosition()).getId())));
                        startActivity(MainActivity.class);
                        finish();
                        return;
                    }
                    mSsoHandler = new SsoHandler(AccountActivity.this);
                    mSsoHandler.authorize(new MyWbAuthListener());
                }
            });
        }
    }
}
