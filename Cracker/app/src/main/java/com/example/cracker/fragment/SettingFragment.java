package com.example.cracker.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.example.cracker.CalendarActivity;
import com.example.cracker.LoginActivity;
import com.example.cracker.MycollActivity;
import com.example.cracker.R;

import cn.bmob.v3.BmobUser;


public class SettingFragment extends Fragment implements View.OnClickListener {
    private FragmentActivity context;
    private LinearLayout ll_course;
    private LinearLayout ll_calendar;
    private LinearLayout ll_logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_set, container,
                false);
        context = getActivity();
        initView(view);
        return view;
    }

    private void initView(View view) {
        ll_course = view.findViewById(R.id.ll_course);
        ll_calendar = view.findViewById(R.id.ll_calendar);
        ll_logout = view.findViewById(R.id.ll_logout);
        ll_course.setOnClickListener(this);
        ll_calendar.setOnClickListener(this);
        ll_logout.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_logout:
                BmobUser user = BmobUser.getCurrentUser();
                if (user==null) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Tips")
                            .setMessage("log in")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    BmobUser.logOut();
                                    startActivity(new Intent(getActivity(), LoginActivity.class));


                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                }else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Tips")
                            .setMessage("LogOut")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    BmobUser.logOut();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                }
                break;


            case R.id.ll_course:
                startActivity(new Intent(getActivity(), MycollActivity.class));
                break;
            case R.id.ll_calendar:
                startActivity(new Intent(getActivity(), CalendarActivity.class));
                break;
        }
    }
}
