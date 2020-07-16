package com.example.playandroid.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.playandroid.R;
import com.example.playandroid.adapter.TextAdapter;
import com.example.playandroid.database.DBUtil;
import com.example.playandroid.entity.Text;
import com.example.playandroid.executor.MyThreadPool;
import com.example.playandroid.manager.FragmentValuesManager;
import com.example.playandroid.manager.LoadDataManger;
import com.example.playandroid.presenter.TextPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /*由于activity动态加载fragment时，fragment的onCreateView
     *方法总在activity中调用replace的那个方法之后，且监听事件需要
     * activity中的数据和控制主布局中某些控件，故用广播来为fragment
     * 布局中的控件添加监听事件
     *
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int message = intent.getIntExtra(FragmentValuesManager.BROADCAST_MESSAGE_KEY, 0);
            switch (message) {
                case FragmentValuesManager.TEXT_FRAGMENT:
                    initHomeFragment();
                    break;
                case FragmentValuesManager.KNOWLEDGE_FRAGMENT:


            }
        }
    };

    private List<Text> textList = new ArrayList<>();
    private TextAdapter adapter;
    private int page = 0;
    private LinearLayout topLayout;
    private LinearLayout bottomLayout;
    private RelativeLayout loadLayout;
    private RelativeLayout freshLayout;
//    private boolean canFresh = true;
    private int fragmentCode = FragmentValuesManager.TEXT_FRAGMENT;



    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case LoadDataManger.LOAD_TEXT_SUCCESS:
                    adapter = new TextAdapter(textList);
                    initHomeFragment();
                    break;
                case LoadDataManger.START_FRESH:
                    setRelativeLayoutVisible(freshLayout);
                    break;
                case LoadDataManger.END_FRESH:
                    setRelativeLayoutGone(freshLayout);
//                    Toast.makeText(MainActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();
                    break;
                case LoadDataManger.START_LOADING:
                    setRelativeLayoutVisible(loadLayout);
                    break;
                case LoadDataManger.END_LOADING:
                    setRelativeLayoutGone(loadLayout);
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        topLayout = findViewById(R.id.top_layout);
        bottomLayout = findViewById(R.id.bottom_layout);
        loadLayout = findViewById(R.id.loading_layout);
        freshLayout = findViewById(R.id.fresh_layout);
        Button home = findViewById(R.id.home_button);
        Button knowledgeSystem = findViewById(R.id.knowledge_button);
        Button item = findViewById(R.id.item_button);
        home.setOnClickListener(this);
        knowledgeSystem.setOnClickListener(this);
        item.setOnClickListener(this);
        replaceFragment(new HomeFragment());
        freshText();
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
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.commit();
    }

    // 初始化文章页面
    private void initHomeFragment() {
        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_layout);
        if (fragment != null) {
            View view = fragment.getView();
            if (view != null) {
                RecyclerView recyclerView = view.findViewById(R.id.text_list);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                // 向上滑动，底部菜单栏和顶部搜索栏出现，下滑则消失
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (dy < 0) {
                            topLayout.setVisibility(View.VISIBLE);
                            bottomLayout.setVisibility(View.VISIBLE);
                            if (!recyclerView.canScrollVertically(-1)) freshText();
                        } else if (dy > 0) {
                            topLayout.setVisibility(View.GONE);
                            bottomLayout.setVisibility(View.GONE);
                            if (!recyclerView.canScrollVertically(1)) setTexts();
                        }
                    }
                });


            }
        }
    }

    private void setRelativeLayoutVisible(RelativeLayout layout) {
        if (layout.getVisibility() != View.VISIBLE) layout.setVisibility(View.VISIBLE);
    }

    private void setRelativeLayoutGone(RelativeLayout layout) {
        if (layout.getVisibility() == View.VISIBLE) layout.setVisibility(View.GONE);
    }

    // 加载文章
    private void setTexts() {
        MyThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                sendMessageAboutText(LoadDataManger.START_LOADING);
                List<Text> texts = TextPresenter.getTexts(page, textList);
                if (!texts.isEmpty()) {
                    textList.addAll(texts);
                    sendMessageAboutText(LoadDataManger.LOAD_TEXT_SUCCESS);
                    DBUtil.writeToLocal(texts, MainActivity.this);
                } else {
                    page ++;
                    setTexts();
                }
                sendMessageAboutText(LoadDataManger.END_LOADING);
            }
        });

    }

    public void sendMessageAboutText(int code) {
        Message message = Message.obtain();
        message.what = code;
        mHandler.sendMessage(message);
    }

    // 拉到顶部刷新，将获得的数据放在RecyclerView的最上面
    public void freshText() {
        Log.d("TAG", "freshText: ");
        MyThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (textList.isEmpty()) {
                    List<Text> localText = DBUtil.loadTextFromLocal(MainActivity.this);
                    if (!localText.isEmpty()) {
                        textList.addAll(localText);
                        sendMessageAboutText(LoadDataManger.LOAD_TEXT_SUCCESS);
                    }
                }
                sendMessageAboutText(LoadDataManger.START_FRESH);
                List<Text> texts = TextPresenter.getTexts(0, textList);
                sendMessageAboutText(LoadDataManger.END_FRESH);
                if (!texts.isEmpty()) {
                    textList.addAll(0, texts);
                    sendMessageAboutText(LoadDataManger.END_FRESH);
                    sendMessageAboutText(LoadDataManger.LOAD_TEXT_SUCCESS);
                    DBUtil.writeToLocal(texts, MainActivity.this);
                }
            }
        });
    }



    private boolean isLastClickedButton(int code) {
        return code == fragmentCode;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_button:
               if (!isLastClickedButton(FragmentValuesManager.TEXT_FRAGMENT)) {
                   replaceFragment(new HomeFragment());
                   fragmentCode = FragmentValuesManager.TEXT_FRAGMENT;
               }
               break;
            case R.id.knowledge_button:
                if (!isLastClickedButton(FragmentValuesManager.KNOWLEDGE_FRAGMENT)) {
                    replaceFragment(new KnowledgeFragment());
                    fragmentCode = FragmentValuesManager.KNOWLEDGE_FRAGMENT;
                }
        }
    }
}
