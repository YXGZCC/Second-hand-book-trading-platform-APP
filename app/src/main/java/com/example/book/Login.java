package com.example.book;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = usernameEditText.getText().toString();
                String psw = passwordEditText.getText().toString();

                if (uname.isEmpty() || psw.isEmpty()) {
                    Toast.makeText(Login.this, "请填写所有字段", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = db.query("tb_users", new String[]{"user_id", "user_name", "password"}, "user_name=? AND password=?", new String[]{uname, psw}, null, null, null);
                if (cursor.moveToFirst()) {
                    int userId = cursor.getInt(cursor.getColumnIndex("user_id"));
                    String userName = cursor.getString(cursor.getColumnIndex("user_name"));

                    // 更新 is_logged_in 字段
                    ContentValues values = new ContentValues();
                    values.put("is_logged_in", 1);
                    db.update("tb_users", values, "user_id=?", new String[]{String.valueOf(userId)});

                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("user_id", userId);
                    intent.putExtra("user_name", userName);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }
}
