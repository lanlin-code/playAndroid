package com.example.playandroid.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.playandroid.R;
import com.example.playandroid.manager.FragmentBroadcastManager;
import com.example.playandroid.net.Request;
import com.example.playandroid.net.RequestBody;

public class MainActivity extends AppCompatActivity {

    /*由于activity动态加载fragment时，fragment的onCreateView
     *方法总在activity中调用replace的那个方法之后，且监听事件需要
     * activity中的数据和控制主布局中某些控件，故用广播来为fragment
     * 布局中的控件添加监听事件
     *
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int message = intent.getIntExtra(FragmentBroadcastManager.BROADCAST_MESSAGE_KEY, 0);
            switch (message) {
                case FragmentBroadcastManager.TEXT_FRAGMENT_FINISH:
                    initHomeFragment();
                    break;

            }
        }
    };

    private LinearLayout topLayout;
    private LinearLayout bottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        topLayout = findViewById(R.id.top_layout);
        bottomLayout = findViewById(R.id.bottom_layout);
        replaceFragment(new HomeFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.playandroid.FRAGMENT_LAYOUT_FINISH");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    // 替换fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }

    private void initHomeFragment() {
        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (fragment != null) {
            View view = fragment.getView();
            if (view != null) {
                RecyclerView recyclerView = view.findViewById(R.id.text_list);
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (dy < 0) {
                            topLayout.setVisibility(View.VISIBLE);
                            bottomLayout.setVisibility(View.VISIBLE);
                        } else if (dy > 0) {
                            topLayout.setVisibility(View.GONE);
                            bottomLayout.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }
    }
}
