package com.example.playandroid.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.playandroid.adapter.TextAdapter;
import com.example.playandroid.entity.Text;
import com.example.playandroid.manager.LoadDataManager;

import com.example.playandroid.R;
import com.example.playandroid.executor.MyThreadPool;
import com.example.playandroid.presenter.HotWordPresenter;
import com.example.playandroid.presenter.TextPresenter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private String keyword;
    private int currentPage = 0;
    private RecyclerView recyclerView;
    private EditText editText;
    private List<String> hotWord;
    private RelativeLayout loadLayout;
    private FlowLayout flowLayout;
    private List<Text> texts;
    private TextAdapter adapter;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case LoadDataManager.LOAD_TEXT_SUCCESS:
                    if (recyclerView.getVisibility() == View.GONE) {
                        recyclerView.setVisibility(View.VISIBLE);
                        flowLayout.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                case LoadDataManager.START_LOADING:
                    loadLayout.setVisibility(View.VISIBLE);
                    break;
                case LoadDataManager.END_LOADING:
                    loadLayout.setVisibility(View.GONE);
                    break;
                case LoadDataManager.LOAD_HOT_WORD_SUCCESS:
                    initFlowLayout();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        hotWord = new ArrayList<>();
        getHotWord();
        flowLayout = findViewById(R.id.flow_layout);
        ImageButton back = findViewById(R.id.search_back);
        ImageButton search = findViewById(R.id.search_in_activity_search);
        editText = findViewById(R.id.search_text);
        loadLayout = findViewById(R.id.search_load);
        search.setOnClickListener(this);
        back.setOnClickListener(this);
        recyclerView = findViewById(R.id.search_list);
        texts = new ArrayList<>();
        adapter = new TextAdapter(texts);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && !recyclerView.canScrollVertically(1)) loadText();
            }
        });
    }


    private void getHotWord() {
        MyThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                List<String> word = HotWordPresenter.getHotWord();
                if (!word.isEmpty()) hotWord.addAll(word);
                sendMessage(LoadDataManager.LOAD_HOT_WORD_SUCCESS);

            }
        });
    }

    @Override
    public void onBackPressed() {

        if (!TextUtils.isEmpty(keyword)) {
            recyclerView.setVisibility(View.GONE);
            flowLayout.setVisibility(View.VISIBLE);
            keyword = null;
            currentPage = 0;
            removeAll();
        } else finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_back:
                onBackPressed();
                break;
            case R.id.search_in_activity_search:
                keyword = editText.getText().toString();
                loadText();
                break;
        }
    }

    private void sendMessage(int code) {
        Message message = Message.obtain();
        message.what = code;
        handler.sendMessage(message);
    }

    private void initFlowLayout() {
        for (String word : hotWord) {
            final TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_flowlayout_item,
                    flowLayout, false);
            textView.setText(word);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    keyword = textView.getText().toString();
                    loadText();
                }
            });
            flowLayout.addView(textView);
        }
    }

    private void loadText() {
        MyThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                sendMessage(LoadDataManager.START_LOADING);
                List<Text> textList = TextPresenter.getSearchText(currentPage, keyword);
                texts.addAll(textList);
                currentPage ++;
                sendMessage(LoadDataManager.END_LOADING);
                sendMessage(LoadDataManager.LOAD_TEXT_SUCCESS);
            }
        });
    }

    private void removeAll() {
        Iterator<Text> iterator = texts.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        adapter.notifyDataSetChanged();
    }
}
