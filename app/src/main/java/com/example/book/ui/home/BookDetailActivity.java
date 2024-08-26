package com.example.book.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.book.DatabaseHelper;
import com.example.book.R;

public class BookDetailActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private int bookId;
    private String bookImage;
    private String userImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        // 获取传递的书籍ID
        bookId = getIntent().getIntExtra("bookId", -1);

        // 初始化UI组件
        ImageView bookImageView = findViewById(R.id.book_image);
        ImageView userImageView = findViewById(R.id.user_image);
        TextView bookNameTextView = findViewById(R.id.book_name);
        TextView authorTextView = findViewById(R.id.author);
        TextView priceTextView = findViewById(R.id.price);
        TextView publisherTextView = findViewById(R.id.publisher);
        TextView publicationYearTextView = findViewById(R.id.publication_year);
        TextView wearDegreeTextView = findViewById(R.id.wear_degree);
        TextView usernameTextView = findViewById(R.id.username);
        Button buyButton = findViewById(R.id.buy_button);

        // 加载书籍详情
        Cursor cursor = db.query("tb_books", null, "BookID = ?", new String[]{String.valueOf(bookId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String bookName = cursor.getString(cursor.getColumnIndexOrThrow("BookName"));
            String author = cursor.getString(cursor.getColumnIndexOrThrow("Author"));
            double currentPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("CurrentPrice"));
            String publisher = cursor.getString(cursor.getColumnIndexOrThrow("Publisher"));
            int publicationYear = cursor.getInt(cursor.getColumnIndexOrThrow("PublicationYear"));
            double wearDegree = cursor.getDouble(cursor.getColumnIndexOrThrow("WearDegree"));
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("UserId"));

            // 获取用户名
            String username = getUsernameById(userId);

            // 获取用户头像
            userImage = getUserImageById(userId);

            // 设置UI组件
            bookNameTextView.setText(bookName);
            authorTextView.setText(author);
            priceTextView.setText("价格：" + currentPrice);
            publisherTextView.setText("出版社：" + publisher);
            publicationYearTextView.setText("出版年份：" + publicationYear);
            wearDegreeTextView.setText("磨损程度：" + wearDegree);
            usernameTextView.setText("发布者：" + username);

            // 假设书籍图片和用户头像是固定的占位符
            bookImageView.setImageResource(R.drawable.ic_book_placeholder);
            userImageView.setImageResource(R.drawable.ic_user_placeholder);

            cursor.close();
        }

        // 购买按钮点击事件
        buyButton.setOnClickListener(v -> {
            Intent intent = new Intent(BookDetailActivity.this, SubmitOrderActivity.class);
            intent.putExtra("bookId", bookId);
            startActivity(intent);
        });
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

    private String getUserImageById(int userId) {
        String userImage = null;
        Cursor cursor = db.query("tb_users", new String[]{"user_picture"}, "user_id = ?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                userImage = cursor.getString(cursor.getColumnIndexOrThrow("user_picture"));
            }
            cursor.close();
        }
        return userImage;
    }
}
