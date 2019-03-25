package com.example.cracker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cracker.CourseDetailActivity;
import com.example.cracker.R;
import com.example.cracker.SearchActivity;
import com.example.cracker.adapter.CourseAdapter;
import com.example.cracker.bean.Course;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private SwipeRefreshLayout swiperefreshlayout;
    private RecyclerView recyclerview;
    private CourseAdapter adapter;
    private List<Course> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        initView(view);
        getData();
        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SearchActivity.class));
            }
        });
        swiperefreshlayout = (SwipeRefreshLayout)view.findViewById(R.id.swiperefreshlayout);
        recyclerview = (RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        adapter = new CourseAdapter(getActivity(),list);
        recyclerview.setAdapter(adapter);
        swiperefreshlayout.setOnRefreshListener(this);
        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Course data = list.get(position);
                Intent intent = new Intent();
                intent.putExtra("data",data);
                intent.setClass(getActivity(), CourseDetailActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRefresh() {
        getData();
    }
    private void getData() {
        BmobQuery<Course> query = new BmobQuery<Course>();
        query.order("-createdAt");
        query.findObjects(new FindListener<Course>() {

            @Override
            public void done(List<Course> diaries, BmobException e) {
                if (e == null) {
                    list.clear();
                    for (Course tr : diaries) {
                        list.add(tr);
                    }
                    swiperefreshlayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                } else {
                    swiperefreshlayout.setRefreshing(false);
                }
            }

        });
    }
}
