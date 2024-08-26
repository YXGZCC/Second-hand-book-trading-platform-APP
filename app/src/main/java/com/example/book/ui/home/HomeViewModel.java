package com.example.book.ui.home;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

public class HomeViewModel extends ViewModel {
    private RecyclerView.Adapter adapter;

    public HomeViewModel() {
        // 初始化adapter
        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
            // 实现adapter的方法
        };
    }

    public void search(String query) {
        // 实现搜索功能
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }
}
