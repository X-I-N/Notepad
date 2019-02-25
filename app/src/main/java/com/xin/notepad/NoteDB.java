package com.xin.notepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NoteDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME_NOTES = "note";

    public NoteDB(Context context){
        super(context,"note",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table note("
                + "id integer primary key autoincrement,"
                + "content "+ "text not null default\"\"" + ")";

        Log.d("SQL", sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
