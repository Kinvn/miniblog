package com.kinvn.miniblog.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.kinvn.miniblog.R;
import com.kinvn.miniblog.base.BaseActivity;
import com.kinvn.miniblog.module.fragment.MessageFragment;
import com.kinvn.miniblog.module.fragment.TimelineFragment;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.main_view_pager)
    ViewPager mViewPage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        initViews();
    }

    private void init() {

    }

    private void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        List<Fragment> list = new ArrayList<>();
        list.add(new TimelineFragment());
        list.add(new MessageFragment());
        mViewPage.setOffscreenPageLimit(list.size());
        mViewPage.setCurrentItem(0);
        mViewPage.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), list, null));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            SsoHandler ssoHandler = new SsoHandler(this);
            ssoHandler.authorize(new MyWbAuthListener());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick(R.id.button_switch_weibo_group)
    void switchGroup() {
        if (mViewPage.getCurrentItem() == 0) {
            // TODO: 2017/8/28
        } else {
            mViewPage.setCurrentItem(0);
        }
    }

    @OnClick(R.id.button_message)
    void switchMessage() {
        mViewPage.setCurrentItem(1);
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;
        private List<String> titles;

        MyFragmentAdapter(FragmentManager fm, List<Fragment> list, List<String> titles) {
            super(fm);
            fragmentList = list;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }
}
