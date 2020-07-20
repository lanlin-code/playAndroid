package com.example.playandroid.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.playandroid.R;
import com.example.playandroid.adapter.KnowledgeAdapter;
import com.example.playandroid.adapter.KnowledgeTextAdapter;
import com.example.playandroid.adapter.TextAdapter;
import com.example.playandroid.entity.Knowledge;
import com.example.playandroid.entity.KnowledgeSystem;
import com.example.playandroid.entity.KnowledgeText;
import com.example.playandroid.entity.Text;
import com.example.playandroid.executor.MyThreadPool;
import com.example.playandroid.manager.DataTransferManager;
import com.example.playandroid.manager.LoadDataManger;
import com.example.playandroid.presenter.KnowledgePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class KnowledgeActivity extends AppCompatActivity {

    private ProgressBar freshBar;
    private RelativeLayout loadingLayout;
    private KnowledgeSystem data;
    private ViewPager viewPager;
    private HashMap<Knowledge, KnowledgeTextAdapter> adapters;
    private List<View> viewList;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case LoadDataManger.START_FRESH:
                    freshBar.setVisibility(View.VISIBLE);
                    break;
                case LoadDataManger.END_FRESH:
                    freshBar.setVisibility(View.GONE);
                    break;
                case LoadDataManger.START_LOADING:
                    loadingLayout.setVisibility(View.VISIBLE);
                    break;
                case LoadDataManger.END_LOADING:
                    loadingLayout.setVisibility(View.GONE);
                    break;
                case LoadDataManger.LOAD_KNOWLEDGE_SUCCESS:
                    Knowledge k = (Knowledge) msg.obj;
                    if (k != null) {
                        KnowledgeTextAdapter adapter = adapters.get(k);
                        if (adapter != null) adapter.notifyDataSetChanged();
                    }
                    break;
                case LoadDataManger.LOAD_KNOWLEDGE_TEXT_FIRSTLY:
                    Knowledge knowledge = (Knowledge) msg.obj;
                    initRecyclerView(knowledge);
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        Intent intent = getIntent();
        data = intent.getParcelableExtra(DataTransferManager.KEY);
        ImageButton back = findViewById(R.id.knowledge_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        freshBar = findViewById(R.id.knowledge_fresh_bar);
        loadingLayout = findViewById(R.id.knowledge_loading_layout);
        TextView title = findViewById(R.id.knowledge_system_title);
        if (data != null) title.setText(data.getName());
        viewPager = findViewById(R.id.knowledge_page);
        adapters = new HashMap<>();
        initPageView();
        loadTextFirstly();
    }


    private void initPageView() {
        List<Knowledge> knowledgeList = data.getKnowledgeList();
        viewList = new ArrayList<>();
        for (int i = 0; i < knowledgeList.size(); i ++) {
            View view = LayoutInflater.from(this).inflate(R.layout.linearlayout_knowledge_recycler, null);
            viewList.add(view);
        }
        KnowledgeAdapter adapter = new KnowledgeAdapter(knowledgeList, viewList);
        viewPager.setAdapter(adapter);
    }


    private void initRecyclerView(final Knowledge knowledge) {
        Log.d("TAG", "initRecyclerView: ");
        List<Knowledge> knowledgeList = data.getKnowledgeList();
        int position = knowledgeList.indexOf(knowledge);
        View view = viewList.get(position);
        RecyclerView recyclerView = view.findViewById(R.id.knowledge_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        KnowledgeTextAdapter adapter = new KnowledgeTextAdapter(knowledge.getTexts());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && !recyclerView.canScrollVertically(-1)) fresh(knowledge);
                else if (dy > 0 && !recyclerView.canScrollVertically(1)) loadMore(knowledge);
            }
        });
        KnowledgeAdapter pageAdapter = (KnowledgeAdapter) viewPager.getAdapter();
        if (pageAdapter != null) pageAdapter.notifyDataSetChanged();
    }

    // 活动启动时调用
    private void loadTextFirstly() {
        List<Knowledge> knowledgeList = data.getKnowledgeList();
        for (final Knowledge knowledge : knowledgeList) {
            MyThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    List<KnowledgeText> texts = KnowledgePresenter.freshData(knowledge);
                    if (!texts.isEmpty()) {
                        knowledge.getTexts().addAll(texts);
                        int currentPage = knowledge.getPage() + 1;
                        knowledge.setPage(currentPage);
                        sendMessage(LoadDataManger.LOAD_KNOWLEDGE_TEXT_FIRSTLY, knowledge);
                    }

                }
            });
        }
    }

    // 刷新数据
    private void fresh(final Knowledge knowledge) {
        MyThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                sendMessage(LoadDataManger.START_FRESH, null);
                List<KnowledgeText> knowledgeTexts = KnowledgePresenter.freshData(knowledge);
                if (!knowledgeTexts.isEmpty()) {
                    knowledge.getTexts().addAll(0, knowledgeTexts);
                    int currentPage = knowledge.getPage() + 1;
                    knowledge.setPage(currentPage);
                    sendMessage(LoadDataManger.LOAD_KNOWLEDGE_SUCCESS, knowledge);
                }
                sendMessage(LoadDataManger.END_FRESH, null);
            }
        });
    }

    // 加载更多
    private void loadMore(final Knowledge knowledge) {
        MyThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                sendMessage(LoadDataManger.START_LOADING, null);
                List<KnowledgeText> knowledgeTexts = KnowledgePresenter.loadMore(knowledge);
                if (!knowledgeTexts.isEmpty()) {
                    knowledge.getTexts().addAll(knowledgeTexts);
                    int currentPage = knowledge.getPage() + 1;
                    knowledge.setPage(currentPage);
                    sendMessage(LoadDataManger.LOAD_KNOWLEDGE_SUCCESS, knowledge);
                }
                sendMessage(LoadDataManger.END_LOADING, null);
            }
        });
    }


    private void sendMessage(int code, Knowledge knowledge) {
        Message message = Message.obtain();
        message.what = code;
        message.obj = knowledge;
        mHandler.sendMessage(message);
    }



}
