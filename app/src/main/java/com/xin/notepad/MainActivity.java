package com.xin.notepad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = "MainActivity";
    private Context mContext;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> dataList;
    private FloatingActionButton fab;
    private NoteDB noteDB;
    private SQLiteDatabase dbread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.list_view);
        dataList = new ArrayList<>();
        fab = findViewById(R.id.fab);
        mContext = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteEdit.ENTER_STATE = 0;
                Intent intent = new Intent(mContext, NoteEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("info", "");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        noteDB = new NoteDB(this);
        dbread = noteDB.getReadableDatabase();
        RefreshNoteList();

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    public void RefreshNoteList() {
        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            arrayAdapter.notifyDataSetChanged();
            listView.setAdapter(arrayAdapter);
        }
        arrayAdapter = new ArrayAdapter<>(mContext, R.layout.note_item, getData());
        listView.setAdapter(arrayAdapter);

    }

    private List<String> getData() {
        Cursor cursor = dbread.query("note", null, "content!=\"\"",
                null, null, null, null);

        while (cursor.moveToNext()) {
            String str = cursor.getString(cursor.getColumnIndex("content"));
            dataList.add(str);
        }

        cursor.close();
        return dataList;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            RefreshNoteList();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NoteEdit.ENTER_STATE = 1;
        String content = dataList.get(position) + "";
        Log.d(TAG, content);

        Cursor c = dbread.query("note", null, "content=" + "'" + content + "'",
                null, null, null, null);
        while (c.moveToNext()) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("info", content);
            String s = c.getString(c.getColumnIndex("id"));
            NoteEdit.id = Integer.parseInt(s);
            intent.putExtras(bundle);
            intent.setClass(MainActivity.this, NoteEdit.class);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
        noteDB = new NoteDB(mContext);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除该日志");
        builder.setMessage("确定删除吗");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = listView.getItemAtPosition(position) + "";
                Cursor c = dbread.query("note", null, "content=" + "'" + content + "'",
                        null, null, null, null);
                while (c.moveToNext()) {
                    String _id = c.getString(c.getColumnIndex("id"));
                    SQLiteDatabase db = noteDB.getWritableDatabase();
                    db.delete("note", "id = ?", new String[]{_id,});
                    RefreshNoteList();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
        return true;
    }
}
