package com.example.cracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.cracker.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText etName;
    private EditText etPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        onSetTitle("login");
        init();
    }

    private void init() {
        etName = (EditText)findViewById(R.id.id_et_username);
        etPwd = (EditText)findViewById(R.id.id_et_password);
        Button mBtnREgister = (Button) findViewById(R.id.id_btn_register);
        mBtnREgister.setOnClickListener(this);
    }

    // if user click submit
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // check the user name and password
            case R.id.id_btn_register:
                String name = etName.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    onToast("Username can not be empty");
                    return;
                }
                if (TextUtils.isEmpty(pwd)){
                    onToast("password can not be empty");
                    return;
                }
                // if username and pwd are valid, record the user info in database
                User user = new User();
                user.setUsername(name);
                user.setPassword(pwd);
                user.signUp(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if (e==null){
                            onToast("registration success");
                            finish();
                        }else{
                            onToast("registration failed"+e.getLocalizedMessage());
                        }
                    }
                });

                break;

        }
    }
}
