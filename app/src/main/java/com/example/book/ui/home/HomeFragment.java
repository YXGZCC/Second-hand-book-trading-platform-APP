package com.example.book.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book.AddBookActivity;
import com.example.book.DatabaseHelper;
import com.example.book.R;
import com.example.book.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private ProductAdapter productAdapter;
    private RecyclerView productList; // 修改变量名为 productList

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 获取UI组件
        EditText searchInput = binding.searchInput;
        Button searchButton = binding.searchButton;
        productList = binding.productList; // 正确初始化变量
        Button addBookButton = binding.addBookButton;

        // 初始化数据库
        dbHelper = new DatabaseHelper(getContext());
        db = dbHelper.getReadableDatabase();

        // 设置RecyclerView
        productAdapter = new ProductAdapter(new ArrayList<>(), book -> {
            // 点击事件处理，跳转到提交订单页面
            Intent intent = new Intent(getActivity(), SubmitOrderActivity.class);
            intent.putExtra("bookId", book.getBookId());
            startActivity(intent);
        });
        productList.setLayoutManager(new LinearLayoutManager(getContext()));
        productList.setAdapter(productAdapter);

        // 加载书籍数据
        loadBooks();

        // 搜索功能
        searchButton.setOnClickListener(v -> searchBooks(searchInput.getText().toString()));

        // 跳转到发布页面
        addBookButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddBookActivity.class);
            startActivity(intent);
        });

        return root;
    }

    private void loadBooks() {
        List<Book> books = new ArrayList<>();
        Cursor cursor = db.query("tb_books", new String[]{"BookID", "BookName", "Author", "CurrentPrice", "UserId"}, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int bookId = cursor.getInt(cursor.getColumnIndexOrThrow("BookID"));
                String bookName = cursor.getString(cursor.getColumnIndexOrThrow("BookName"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("Author"));
                double currentPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("CurrentPrice"));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("UserId"));
                String username = getUsernameById(userId); // 从userId找到对应user_name，将user_name传到界面显示

                books.add(new Book(bookId, bookName, currentPrice, author, username));
            }
            cursor.close();
        }

        productAdapter.updateBooks(books); // 更新适配器数据
    }

    private void searchBooks(String query) {
        // 实现搜索功能
    }

    private String getUsernameById(int userId) {
        String username = null;
        Cursor cursor = db.query("tb_users", new String[]{"user_name"}, "user_id = ?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                username = cursor.getString(cursor.getColumnIndexOrThrow("user_name"));
            }
            cursor.close();
        }
        return username;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        db.close();
    }
}
