package com.example.cracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cracker.adapter.NoteListAdapter;
import com.example.cracker.bean.Note;
import com.example.cracker.utils.DBDao;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends BaseActivity implements CalendarView.OnYearChangeListener, CalendarView.OnCalendarSelectListener {

    TextView mTextMonthDay;

    TextView mTextYear;


    TextView mTextCurrentDay;
    private int mYear;
    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private CalendarLayout mCalendarLayout;
    private RecyclerView mRecyclerView;
    private List<Note> list = new ArrayList<>();
    private NoteListAdapter mAdapter;
    private ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initView();
    }

    private void initView() {
        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTextMonthDay = (TextView)findViewById(R.id.tv_month_day);
        mTextYear = (TextView)  findViewById(R.id.tv_year);
        mRelativeTool = (RelativeLayout) findViewById(R.id.rl_tool);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mTextCurrentDay = (TextView) findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });

        mCalendarLayout = (CalendarLayout) findViewById(R.id.calendarLayout);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnCalendarLongClickListener(new CalendarView.OnCalendarLongClickListener() {
            @Override
            public void onCalendarLongClickOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarLongClick(Calendar calendar) {
                Intent intent = new Intent();
                intent.putExtra("year",calendar.getYear()+"" );
                intent.putExtra("month",calendar.getMonth() +"");
                intent.putExtra("day",calendar.getDay() +"");
                intent.setClass(CalendarActivity.this,AddNoteActivity.class);
                startActivity(intent);
            }
        });
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "-" + mCalendarView.getCurDay());
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NoteListAdapter(this, list);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new NoteListAdapter.ItemClickListener() {
            @Override
            public void setOnItemClickListener(int position) {

            }

            @Override
            public void setOnItemLongClickListener(int position) {
                showDel(position);
            }
        });
    }
    private void showDel(final int position) {
        new AlertDialog.Builder(this)
                .setTitle("Tips")
                .setMessage("delete or not")
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        del(position);
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }


        }).create().show();
    }
    private void del(int position) {
        DBDao.getInstance(this).del(list.get(position).getId()+"");
        list.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "-" + calendar.getDay() );
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mYear = calendar.getYear();
        list.clear();
        List<Note> dataList = DBDao.getInstance(this).loadNoteByTime(calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay());
        list.addAll(dataList);
        mAdapter.notifyDataSetChanged();
    }
}
