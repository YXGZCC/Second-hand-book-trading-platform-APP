package com.example.book;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

public class Logout {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public Logout(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void logoutUser(Context context) {
        // 将所有用户的 is_logged_in 字段设置为 0
        ContentValues values = new ContentValues();
        values.put("is_logged_in", 0);
        db.update("tb_users", values, null, null);

        // 返回登录页面
        Intent intent = new Intent(context, Login.class);
        context.startActivity(intent);
    }
}
