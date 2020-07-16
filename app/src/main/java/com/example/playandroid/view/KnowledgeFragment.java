package com.example.playandroid.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.playandroid.R;
import com.example.playandroid.manager.FragmentValuesManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class KnowledgeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_knowledge_system, container, false);
        Intent intent = new Intent("com.example.playandroid.FRAGMENT_LAYOUT_FINISH");
        intent.putExtra(FragmentValuesManager.BROADCAST_MESSAGE_KEY, FragmentValuesManager.KNOWLEDGE_FRAGMENT);
        FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity != null) fragmentActivity.sendBroadcast(intent);
        return view;
    }
}
