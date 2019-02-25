package com.xin.notepad;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class NoteEdit extends AppCompatActivity {

    private EditText et_content;
    private Button btn_ok;
    private Button btn_cancel;
    private NoteDB noteDB;
    private SQLiteDatabase dbreader;
    public static int ENTER_STATE = 0;
    public static String last_content;
    public static int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        setContentView(R.layout.edit_page);
        if (actionBar != null) {
            actionBar.hide();
        }

        et_content = findViewById(R.id.et_content);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        noteDB = new NoteDB(this);
        dbreader = noteDB.getReadableDatabase();
        Bundle myBundle = this.getIntent().getExtras();
        last_content = myBundle.getString("info");
        Log.d("LAST_CONTENT", last_content);
        et_content.setText(last_content);

        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_content.getText().toString();
                String sql_count = "SELECT COUNT(*) FROM note";
                SQLiteStatement statement = dbreader.compileStatement(sql_count);
                long count = statement.simpleQueryForLong();
                Log.d("COUNT", count + "");
                Log.d("ENTER_STATE", ENTER_STATE + "");
                SQLiteDatabase db = noteDB.getWritableDatabase();

                if (ENTER_STATE == 0) {
                    if (!content.equals("")) {
                        ContentValues values = new ContentValues();
                        values.put("content", content);
                        db.insert(NoteDB.TABLE_NAME_NOTES, null, values);
                        Log.d("note_content", content);
                        values.clear();
                    }
                } else {
                    ContentValues values = new ContentValues();
                    values.put("content", content);
                    String s = String.valueOf(id);
                    Log.d("update", content);
                    db.update("note", values, "id = ?",new String[]{s});
                }

                Intent data = new Intent();
                setResult(RESULT_OK, data);
                finish();
            }
        });
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
