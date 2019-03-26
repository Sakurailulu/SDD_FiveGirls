package com.example.cracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.cracker.adapter.DataAdapter;
import com.example.cracker.bean.Course;
import com.example.cracker.bean.Data;
import com.example.cracker.bean.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


public class CourseDetailActivity extends BaseActivity {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerview;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.fab_edit)
    FloatingActionButton fabEdit;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;
    private CourseDetailActivity context;
    private ArrayList<Data> list = new ArrayList<Data>();
    private DataAdapter adapter;
    private Course data;

    /**
     *  initialize the class
     *  */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_coursedetail);
        ButterKnife.bind(this);
        data = (Course) getIntent().getSerializableExtra("data");
        onSetTitle(data.getName());
        initView();
        getDataList();
    }



    /***
     * initialize the view of course detail page
     */
    private void initView() {
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataList();
            }
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new DataAdapter(this, list);
        adapter.setOnItemClickListener(new DataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(CourseDetailActivity.this,ImageBigActivity.class);
                intent.putExtra("url",list.get(position).getImg());
                startActivity(intent);
            }
        });
        recyclerview.setAdapter(adapter);
        mTvRight.setVisibility(View.VISIBLE);
        User user = BmobUser.getCurrentUser(User.class);
        if (user != null) {
            if (iscoll(data.getObjectId())) {
                mTvRight.setText("Cancel");
            } else {
                mTvRight.setText("Fav");
            }
        }
    }



    // get all the course material
    private void getDataList() {
        BmobQuery<Data> query = new BmobQuery<>();
        query.order("-createdAt");
        query.addWhereEqualTo("course", data);
        query.findObjects(new FindListener<Data>() {

            @Override
            public void done(List<Data> exhibitsList, BmobException e) {
                swiperefreshlayout.setRefreshing(false);
                if (e == null) {
                    list.clear();
                    for (Data tr : exhibitsList) {
                        list.add(tr);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

        });
    }

    /**
     * Controll the button click
     * */
    @OnClick({R.id.tv_right, R.id.fab_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                // Add course to favorite list
                User person = BmobUser.getCurrentUser(User.class);
                if (person == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Not logged in, are you sure to log in?").setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(context, LoginActivity.class));
                        }
                    }).setNegativeButton("cancel", null);
                    builder.show();
                } else {
                    String str = person.getColl();
                    if (TextUtils.isEmpty(str)) {
                        person.setColl(data.getObjectId());
                        person.update(person.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    onToast("Successful collection");
                                    mTvRight.setText("cancel");
                                } else {
                                    onToast("failed:" + e.getMessage());
                                }
                            }
                        });

                    } else {
                        String[] strings = str.split(",,,");
                        boolean flag = false;
                        for (String an : strings) {
                            if (an.equals(data.getObjectId())) {
                                flag = true;
                            }
                        }
                        if (flag) {
                            cancel();
                        } else {
                            person.setColl(str + ",,," + data.getObjectId());
                            person.update(person.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        onToast("Successful collection");
                                        mTvRight.setText("cancel");
                                    } else {
                                        onToast("failed:" + e.getMessage());
                                    }
                                }
                            });
                        }
                    }
                }
                break;
            case R.id.fab_edit:
                // x course matrial
                // if user did not login, will remind user to login first
                User person2 = BmobUser.getCurrentUser(User.class);
                if (person2 == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Not logged in, are you sure to log in?").setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(context, LoginActivity.class));
                        }
                    }).setNegativeButton("cancel", null);
                    builder.show();
                } else {
                    Intent intent =  new Intent(CourseDetailActivity.this, ReleaseActivity.class);
                    intent.putExtra("data",data);
                    startActivity(intent);
                }
                break;
        }
    }

    // remove the course from fav list
    private void cancel() {
        User person = BmobUser.getCurrentUser(User.class);
        String str = person.getColl();
        String[] strings = str.split(",,,");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strings.length; i++) {
            if (!data.getObjectId().equals(strings[i])) {
                sb.append(strings[i] + ",,,");
            }
        }
        person.setColl(sb.toString());
        person.update(person.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    onToast("Cancel successfully");
                    mTvRight.setText("Fav");
                } else {
                    onToast("failed:" + e.getMessage());
                }
            }
        });
    }



    // check if the course is in favorite list
    private boolean iscoll(String id) {
        boolean flag = false;
        User person = BmobUser.getCurrentUser(User.class);
        String str = person.getColl();
        if (str == null) {
            return flag;
        }
        String[] strings = str.split(",,,");


        for (String an : strings) {
            if (an.equals(data.getObjectId())) {
                flag = true;
            }
        }
        return flag;
    }
}
