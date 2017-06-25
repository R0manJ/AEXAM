package com.rjstudio.aexam.UsrInfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by r0man on 2017/6/25.
 */

public class UsrDBOpenHelper extends SQLiteOpenHelper{
    String CREATE_TABLE = "" +
            "create table usr_info" +
            "(USERNAME nvarchar(20)," +
            "SubjectNumber int," +
            "isCorrect int)";

    public UsrDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
