package com.example.book;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Books.db";
    private static final int DATABASE_VERSION = 1;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS tb_users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_name NVARCHAR(20) NOT NULL, " +
                "password NVARCHAR(30) NOT NULL, " +
                "email NVARCHAR(30), " +
                "phone_number NVARCHAR(20), " +
                "sex TINYINT DEFAULT 0, " +  // 设置默认值为 0
                "user_picture NVARCHAR(255), " +
                "birthday DATE, " +
                "autograph NVARCHAR(300), " +
                "is_logged_in INTEGER DEFAULT 0)"); // 添加 is_logged_in 字段

        // 创建订单表
        db.execSQL("CREATE TABLE IF NOT EXISTS tb_orders (" +
                "order_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "buy_user_id INTEGER NOT NULL, " +
                "total_price DECIMAL NOT NULL, " +
                "status NVARCHAR(20) NOT NULL, " +
                "order_time DATETIME NOT NULL, " +
                "payment_time DATETIME , " +
                "notes NVARCHAR(1000), " +
                "delivery_address NVARCHAR(80) NOT NULL, " +
                "sale_user_id INTEGER NOT NULL, " +
                "BookID INTEGER NOT NULL, " +
                "tracking_number NVARCHAR(30), " +
                "FOREIGN KEY(buy_user_id) REFERENCES tb_users(user_id), " +
                "FOREIGN KEY(sale_user_id) REFERENCES tb_users(user_id))");
        // 创建书籍信息表
        db.execSQL("CREATE TABLE IF NOT EXISTS tb_books (" +
                "BookID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "BookName VARCHAR(60) NOT NULL, " +
                "Author VARCHAR(30) NOT NULL, " +
                "ISBN VARCHAR(20) NOT NULL, " +
                "PublicationYear INTEGER NOT NULL, " + // 使用 INTEGER 代替 YEAR
                "Publisher VARCHAR(30) NOT NULL, " +
                "BookCategory VARCHAR(20) NOT NULL, " +
                "OriginalPrice DECIMAL(10,2) NOT NULL, " +
                "CurrentPrice DECIMAL(10,2) NOT NULL, " +
                "WearDegree DECIMAL(3,2) NOT NULL, " +
                "UserId INTEGER NOT NULL)"); // 新增列
        // 创建评论信息表
        db.execSQL("CREATE TABLE IF NOT EXISTS tb_comments (" +
                "CommentID BIGINT PRIMARY KEY NOT NULL, " +
                "BookID BIGINT NOT NULL, " +
                "UserID BIGINT NOT NULL, " +
                "CommentContent TEXT NOT NULL, " +
                "CommentTime DATETIME NOT NULL, " +
                "FOREIGN KEY(BookID) REFERENCES tb_books(BookID), " +
                "FOREIGN KEY(UserID) REFERENCES tb_users(user_id))");




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // 删除旧表并创建新表
        db.execSQL("DROP TABLE IF EXISTS tb_users");
        onCreate(db);
    }
}
