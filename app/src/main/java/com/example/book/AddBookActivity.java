package com.example.book;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddBookActivity extends AppCompatActivity {

    private EditText etBookName, etAuthor, etIsbn, etPublicationYear, etPublisher, etBookCategory, etOriginalPrice, etCurrentPrice, etWearDegree;
    private Button btnSubmit;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book);

        // Initialize database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Bind views
        etBookName = findViewById(R.id.et_book_name);
        etAuthor = findViewById(R.id.et_author);
        etIsbn = findViewById(R.id.et_isbn);
        etPublicationYear = findViewById(R.id.et_publication_year);
        etPublisher = findViewById(R.id.et_publisher);
        etBookCategory = findViewById(R.id.et_book_category);
        etOriginalPrice = findViewById(R.id.et_original_price);
        etCurrentPrice = findViewById(R.id.et_current_price);
        etWearDegree = findViewById(R.id.et_wear_degree);
        btnSubmit = findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookToDatabase();
            }
        });
    }

    private void addBookToDatabase() {
        // Get logged in user_id
        String userId = getLoggedInUserId();
        if (userId == null) {
            Toast.makeText(this, "请先登录再发布书籍", Toast.LENGTH_SHORT).show();
            return;
        }

        String bookName = etBookName.getText().toString();
        String author = etAuthor.getText().toString();
        String isbn = etIsbn.getText().toString();
        String publicationYear = etPublicationYear.getText().toString();
        String publisher = etPublisher.getText().toString();
        String bookCategory = etBookCategory.getText().toString();
        String originalPrice = etOriginalPrice.getText().toString();
        String currentPrice = etCurrentPrice.getText().toString();
        String wearDegree = etWearDegree.getText().toString();

        if (bookName.isEmpty() || author.isEmpty() || isbn.isEmpty() || publicationYear.isEmpty() ||
                publisher.isEmpty() || bookCategory.isEmpty() || originalPrice.isEmpty() || currentPrice.isEmpty() || wearDegree.isEmpty()) {
            Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("BookName", bookName);
        values.put("Author", author);
        values.put("ISBN", isbn);
        values.put("PublicationYear", Integer.parseInt(publicationYear));
        values.put("Publisher", publisher);
        values.put("BookCategory", bookCategory);
        values.put("OriginalPrice", Double.parseDouble(originalPrice));
        values.put("CurrentPrice", Double.parseDouble(currentPrice));
        values.put("WearDegree", Double.parseDouble(wearDegree));
        values.put("UserId", userId); // 添加 user_id 字段

        long newRowId = db.insert("tb_books", null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "书籍信息发布成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "发布失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }

    private String getLoggedInUserId() {
        String userId = null;
        Cursor cursor = db.query("tb_users", new String[]{"user_id"}, "is_logged_in = ?", new String[]{"1"}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                userId = cursor.getString(cursor.getColumnIndex("user_id"));
            }
            cursor.close();
        }
        return userId;
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
