package com.example.playandroid.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.playandroid.R;
import com.example.playandroid.adapter.CategoryAdapter;
import com.example.playandroid.adapter.ItemAdapter;
import com.example.playandroid.adapter.KnowledgeSystemAdapter;
import com.example.playandroid.adapter.TextAdapter;
import com.example.playandroid.database.DBUtil;
import com.example.playandroid.entity.Category;
import com.example.playandroid.entity.Item;
import com.example.playandroid.entity.KnowledgeSystem;
import com.example.playandroid.entity.Text;
import com.example.playandroid.executor.MyThreadPool;
import com.example.playandroid.manager.FragmentValuesManager;
import com.example.playandroid.manager.LoadDataManger;
import com.example.playandroid.presenter.CategoryPresenter;
import com.example.playandroid.presenter.KnowledgeSystemPresenter;
import com.example.playandroid.presenter.TextPresenter;
import com.example.playandroid.util.ThreadAdjustUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /*
     *由于活动加载碎片的过程总在活动的那个调用了replace的方法之后，
     * 且fragment某些控件需要活动中的数据和对活动的某些控件进行操作
     * 所以用广播来监听fragment创建
     *
     */
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int code = intent.getIntExtra(FragmentValuesManager.BROADCAST_MESSAGE_KEY, FragmentValuesManager.FAIL);
            switch (code) {
                case FragmentValuesManager.TEXT_FRAGMENT:
                    initHomeFragment();
                    break;
                case FragmentValuesManager.KNOWLEDGE_FRAGMENT:
                    if (haveInitKnowledge) initKnowledgeFragment();
                    break;
                case FragmentValuesManager.ITEM_FRAGMENT:
                    if (haveInitItem) initProjectFragment();
            }
        }
    };

    private List<Text> textList = new ArrayList<>();
    private TextAdapter adapter = new TextAdapter(textList);
    private int page = 0;
    private LinearLayout topLayout;
    private LinearLayout bottomLayout;
    private RelativeLayout loadLayout;
    private RelativeLayout freshLayout;
    private int fragmentCode = FragmentValuesManager.TEXT_FRAGMENT;
    private List<KnowledgeSystem> knowledgeSystemList = new ArrayList<>();
    private KnowledgeSystemAdapter knowledgeSystemAdapter = new KnowledgeSystemAdapter(knowledgeSystemList);
    private List<Category> categories = new ArrayList<>();
    private boolean haveInitKnowledge = false;
    private boolean haveInitItem = false;
    private ConcurrentHashMap<Category, ItemAdapter> projectAdapters = new ConcurrentHashMap<>();


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case LoadDataManger.LOAD_TEXT_SUCCESS:
                    adapter.notifyDataSetChanged();
                    initHomeFragment();
                    break;
                case LoadDataManger.START_FRESH:
                    setRelativeLayoutVisible(freshLayout);
                    break;
                case LoadDataManger.END_FRESH:
                    setRelativeLayoutGone(freshLayout);
                    break;
                case LoadDataManger.START_LOADING:
                    setRelativeLayoutVisible(loadLayout);
                    break;
                case LoadDataManger.END_LOADING:
                    setRelativeLayoutGone(loadLayout);
                    break;
                case LoadDataManger.LOAD_KNOWLEDGE_SYSTEM_SUCCESS:
                    knowledgeSystemAdapter.notifyDataSetChanged();
                    initKnowledgeFragment();
                    haveInitKnowledge = true;
                    break;
                case LoadDataManger.LOAD_CATEGORY_SUCCESS:
                    initProjectFragment();
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
        ThreadAdjustUtil.initHandler();
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
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FragmentValuesManager.ACTION);
        registerReceiver(receiver, intentFilter);
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

    // 第一次加载项目界面
    private void getCategoryList() {
        MyThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (categories.isEmpty()) {
                    sendMessage(LoadDataManger.START_FRESH);
                    List<Category> categoryList = CategoryPresenter.getCategoryList();
                    categories.addAll(categoryList);
                    for (Category category : categories) {
                        freshProjectData(category);
                    }
                    sendMessage(LoadDataManger.END_FRESH);
                    sendMessage(LoadDataManger.LOAD_CATEGORY_SUCCESS);
                }

            }
        });
    }

    // 加载Category
    private List<Item> loadCategory(Category category) {
        List<Item> itemList = CategoryPresenter.loadCategory(category);
        if (!itemList.isEmpty()) {
            int currentPage = category.getCurrentPage() + 1;
            category.setCurrentPage(currentPage);
        }
        return itemList;
    }

    // 刷新project界面
    private void freshProjectData(final Category category) {
        MyThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                sendMessage(LoadDataManger.START_FRESH);
                List<Item> itemList = CategoryPresenter.freshCategory(category);
                if (!itemList.isEmpty()) {
                    category.getItems().addAll(0, itemList);
                    int currentPage = category.getCurrentPage() + 1;
                    category.setCurrentPage(currentPage);
                }
                sendMessage(LoadDataManger.END_FRESH);
            }
        });
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
                            showTopAndBottomLayout();
                            if (!recyclerView.canScrollVertically(-1)) freshText();
                        } else if (dy > 0) {
                            hideTopAndBottomLayout();
                            if (!recyclerView.canScrollVertically(1)) setTexts();
                        }
                    }
                });
            }
        }
    }

    // 初始化项目界面
    private void initProjectFragment() {
        if (!haveInitItem) haveInitItem = true;
        ItemFragment fragment = (ItemFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_layout);
        if (fragment != null) {
            View view = fragment.getView();
            if (view != null) {
                final List<View> views = new ArrayList<>();
                for (int i = 0; i < categories.size(); i ++) {
                    View itemView = LayoutInflater.from(this).inflate(R.layout.linearlayout_viewpager_item_view, null);
                    RecyclerView recyclerView = itemView.findViewById(R.id.project_list);
                    LinearLayoutManager manager = new LinearLayoutManager(this);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            if (dy < 0) {
                                showTopAndBottomLayout();
                            } else {
                                hideTopAndBottomLayout();
                            }
                        }
                    });
                    Category category = categories.get(i);
                    ItemAdapter adapter = projectAdapters.get(category);
                    if (adapter == null) {
                        int maxSize = (int) (Runtime.getRuntime().freeMemory()/(4*categories.size()));
                        adapter = new ItemAdapter(category.getItems(), maxSize);
                    }
                    recyclerView.setAdapter(adapter);
                    views.add(itemView);
                }
                ViewPager viewPager = view.findViewById(R.id.fragment_pager);
                CategoryAdapter pageAdapter = new CategoryAdapter(categories, views);
                viewPager.setAdapter(pageAdapter);
            }
        }
    }

    // 加载知识体系数据
    private void loadKnowledgeSystems() {
        MyThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                sendMessage(LoadDataManger.START_FRESH);
                if (knowledgeSystemList.isEmpty()) {
                    List<KnowledgeSystem> data = KnowledgeSystemPresenter.getKnowledgeSystems();
                    sendMessage(LoadDataManger.END_FRESH);
                    if (!data.isEmpty()) {
                        knowledgeSystemList.addAll(data);
                        sendMessage(LoadDataManger.LOAD_KNOWLEDGE_SYSTEM_SUCCESS);
                    }
                }
            }
        });
    }

    // 初始化知识体系fragment
    private void initKnowledgeFragment() {
        KnowledgeFragment fragment = (KnowledgeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_layout);
        if (fragment != null) {
            View view = fragment.getView();
            if (view != null) {
                RecyclerView recyclerView = view.findViewById(R.id.knowledge_system_list);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(knowledgeSystemAdapter);
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (dy < 0) {
                            showTopAndBottomLayout();
                            if (!recyclerView.canScrollVertically(-1)) loadKnowledgeSystems();
                        } else if (dy > 0) {
                            hideTopAndBottomLayout();
                        }
                    }
                });
            }
         }
    }

    private void hideTopAndBottomLayout() {
        topLayout.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);
    }

    private void showTopAndBottomLayout() {
        topLayout.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.VISIBLE);
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
                sendMessage(LoadDataManger.START_LOADING);
                List<Text> texts = TextPresenter.getTexts(page, textList);
                if (!texts.isEmpty()) {
                    textList.addAll(texts);
                    sendMessage(LoadDataManger.LOAD_TEXT_SUCCESS);
                    DBUtil.writeToLocal(texts, MainActivity.this);
                } else {
                    page ++;
                    setTexts();
                }
                sendMessage(LoadDataManger.END_LOADING);
            }
        });

    }

    public void sendMessage(int code) {
        Message message = Message.obtain();
        message.what = code;
        mHandler.sendMessage(message);
    }

    // 拉到顶部刷新，将获得的数据放在RecyclerView的最上面
    public void freshText() {
        MyThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (textList.isEmpty()) {
                    List<Text> localText = DBUtil.loadTextFromLocal(MainActivity.this);
                    if (!localText.isEmpty()) {
                        textList.addAll(localText);
                        sendMessage(LoadDataManger.LOAD_TEXT_SUCCESS);
                    }
                }
                sendMessage(LoadDataManger.START_FRESH);
                List<Text> texts = TextPresenter.getTexts(0, textList);
                sendMessage(LoadDataManger.END_FRESH);
                if (!texts.isEmpty()) {
                    textList.addAll(0, texts);
                    sendMessage(LoadDataManger.END_FRESH);
                    sendMessage(LoadDataManger.LOAD_TEXT_SUCCESS);
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
                    if (knowledgeSystemList.isEmpty()) loadKnowledgeSystems();
                    fragmentCode = FragmentValuesManager.KNOWLEDGE_FRAGMENT;
                }
            case R.id.item_button:
                if (!isLastClickedButton(FragmentValuesManager.ITEM_FRAGMENT)) {
                    replaceFragment(new ItemFragment());
                    if (categories.isEmpty()) getCategoryList();
                    fragmentCode = FragmentValuesManager.ITEM_FRAGMENT;
                }
        }
    }
}
