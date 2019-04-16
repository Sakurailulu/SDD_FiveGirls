package com.example.cracker;

import android.app.Person;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.example.cracker.adapter.CourseAdapter;
import com.example.cracker.bean.Course;
import com.example.cracker.bean.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class MycollActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerview;
    private CourseAdapter adapter;
    private List<Course> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycaoll);
        ButterKnife.bind(this);
        onSetTitle("My course");
        initView();
    }

    private void initView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter = new CourseAdapter(this,list);
        recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Course data = list.get(position);
                Intent intent = new Intent();
                intent.putExtra("data",data);
                intent.setClass(MycollActivity.this, CourseDetailActivity.class);
                startActivity(intent);
            }
        });
        BmobUser user = BmobUser.getCurrentUser();
        if (user==null) {
            onToast("Please login first");
            return;
        }
        getList();
    }

    private void getList( ) {
        BmobQuery<Course> query = new BmobQuery<>();
        query.order("-createdAt");
        String coll = BmobUser.getCurrentUser(User.class).getColl();
        if (TextUtils.isEmpty(coll)) {
            return;
        }
        String[] colls = coll.split(",,,");
        query.addWhereContainedIn("objectId", Arrays.asList(colls));
        query.findObjects(new FindListener<Course>() {

            @Override
            public void done(List<Course> diaries, BmobException e) {
                if (e == null) {
                    list.clear();
                    for (Course tr : diaries) {
                        list.add(tr);
                    }
                    adapter.notifyDataSetChanged();
                } else {

                }
            }

        });
    }


}
