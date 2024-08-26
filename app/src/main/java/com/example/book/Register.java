package com.example.book;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button registerButton;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = usernameEditText.getText().toString();
                String psw = passwordEditText.getText().toString();

                if (uname.isEmpty() || psw.isEmpty()) {
                    Toast.makeText(Register.this, "请填写所有字段", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = db.query("tb_users", new String[]{"user_name"}, "user_name=?", new String[]{uname}, null, null, null);
                if (cursor.moveToFirst()) {
                    Toast.makeText(Register.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                } else {
                    String SQLstr = "INSERT INTO tb_users(user_name, password) VALUES(?, ?)";
                    db.execSQL(SQLstr, new String[]{uname, psw});
                    Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                    finish();  // 结束当前Activity，避免用户返回注册页面
                }
                cursor.close();
            }
        });
    }
}
