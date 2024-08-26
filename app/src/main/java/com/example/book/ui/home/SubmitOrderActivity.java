package com.example.book.ui.home;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.book.DatabaseHelper;
import com.example.book.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SubmitOrderActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private int bookId;
    private double currentPrice;
    private int saleUserId;
    private int buyUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // 获取传递的书籍ID
        bookId = getIntent().getIntExtra("bookId", -1);

        // 初始化UI组件
        ImageView bookImageView = findViewById(R.id.book_image);
        TextView bookNameTextView = findViewById(R.id.book_name);
        TextView priceTextView = findViewById(R.id.price);
        EditText addressEditText = findViewById(R.id.address_input);
        EditText notesEditText = findViewById(R.id.notes_input);
        EditText phoneEditText = findViewById(R.id.phone_input);
        Button submitOrderButton = findViewById(R.id.submit_order_button);

        // 获取当前登录用户ID
        buyUserId = getLoggedInUserId();

        // 加载书籍详情
        Cursor cursor = db.query("tb_books", null, "BookID = ?", new String[]{String.valueOf(bookId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String bookName = cursor.getString(cursor.getColumnIndexOrThrow("BookName"));
            currentPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("CurrentPrice"));
            saleUserId = cursor.getInt(cursor.getColumnIndexOrThrow("UserId"));

            // 设置UI组件
            bookNameTextView.setText(bookName);
            priceTextView.setText("价格：" + currentPrice);

            // 假设书籍图片是固定的占位符
            bookImageView.setImageResource(R.drawable.ic_book_placeholder);

            cursor.close();
        }

        // 提交订单按钮点击事件
        submitOrderButton.setOnClickListener(v -> {
            String address = addressEditText.getText().toString();
            String notes = notesEditText.getText().toString();
            String phone = phoneEditText.getText().toString();

            if (address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                return;
            }

            // 插入订单信息到数据库
            ContentValues values = new ContentValues();
            values.put("buy_user_id", buyUserId);
            values.put("total_price", currentPrice);
            values.put("status", "待支付");
            values.put("order_time", getCurrentTime());
            values.put("notes", notes);
            values.put("delivery_address", address);
            values.put("sale_user_id", saleUserId);
            values.put("BookID", bookId);
            values.put("tracking_number", phone);

            long orderId = db.insert("tb_orders", null, values);
            if (orderId != -1) {
                showPaymentDialog(orderId);
            } else {
                Toast.makeText(this, "订单提交失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getLoggedInUserId() {
        int userId = -1;
        Cursor cursor = db.query("tb_users", new String[]{"user_id"}, "is_logged_in = 1", null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
            cursor.close();
        }
        return userId;
    }

    private void showPaymentDialog(long orderId) {
        new AlertDialog.Builder(this)
                .setTitle("支付确认")
                .setMessage("是否确认支付？")
                .setPositiveButton("确认", (dialog, which) -> {
                    // 更新订单状态为已支付
                    ContentValues values = new ContentValues();
                    values.put("status", "已支付");
                    values.put("payment_time", getCurrentTime());
                    db.update("tb_orders", values, "order_id = ?", new String[]{String.valueOf(orderId)});
                    Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
