package com.example.cracker;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cracker.bean.Note;
import com.example.cracker.utils.DBDao;


public class AddNoteActivity extends BaseActivity  {
    EditText mEtTitle;
    EditText mEtContent;
    TextView mBtAdd;
    private String year;
    private String month;
    private String day;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        year = getIntent().getStringExtra("year");
        month = getIntent().getStringExtra("month");
        day = getIntent().getStringExtra("day");
        initView();
        onSetTitle("calendar");
    }


    private void initView() {
        mEtTitle = findViewById(R.id.et_title);
        mEtContent = findViewById(R.id.et_content);
        mBtAdd = findViewById(R.id.bt_add);
        mBtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mEtTitle.getText().toString();
                String content = mEtContent.getText().toString();
                if (TextUtils.isEmpty(title)){
                    onToast("Please enter a title");
                    return;
                }
                if (TextUtils.isEmpty(content)){
                    onToast("Please enter the content");
                    return;
                }

                DBDao.getInstance(AddNoteActivity.this).insertNote(new Note(title,content,year+"-"+month+"-"+day));
                onToast("Added successfully");
                finish();
            }
        });
    }




}
