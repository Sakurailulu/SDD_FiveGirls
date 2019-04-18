package com.example.cracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cracker.adapter.CourseAdapter;
import com.example.cracker.bean.Course;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class SearchActivity extends BaseActivity {


    @BindView(R.id.at_title)
    TextView mAtTitle;
    @BindView(R.id.at_toolbar)
    Toolbar mAtToolbar;
    @BindView(R.id.et_write_pwd)
    EditText mEtWritePwd;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerview;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    private SearchActivity context;
    private ArrayList<Course> list = new ArrayList<Course>();
    private CourseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_myserach);
        ButterKnife.bind(this);
        onSetTitle("Search");
        initView();
    }


    private void initView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter = new CourseAdapter(this,list);
        recyclerview.setAdapter(adapter);
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText("search");
        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Course data = list.get(position);
                Intent intent = new Intent();
                intent.putExtra("data",data);
                intent.setClass(SearchActivity.this, CourseDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getCommentList(final String content) {
        if (TextUtils.isEmpty(content)) {
            onToast("Please enter key words");
            return;
        }

        BmobQuery<Course> query = new BmobQuery<>();
        query.order("-createdAt");
        query.findObjects(new FindListener<Course>() {

            @Override
            public void done(List<Course> exhibitsList, BmobException e) {
                if (e == null) {
                    list.clear();
                    for (Course tr : exhibitsList) {
                        if (tr.getName().toLowerCase().contains(content.toLowerCase())) {
                            list.add(tr);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

        });
    }



    @OnClick(R.id.tv_right)
    public void onClick() {
        String content = mEtWritePwd.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(context, "Please enter key words", Toast.LENGTH_SHORT).show();
            return;
        }
        getCommentList(content);
    }


}
