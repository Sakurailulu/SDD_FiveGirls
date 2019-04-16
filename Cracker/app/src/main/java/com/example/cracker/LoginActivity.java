package com.example.cracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.cracker.utils.StatusBarUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText etName;
    private EditText etPwd;
    private TextView btLogin;
    private TextView tv_register;
    private LoginActivity context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        StatusBarUtil.setTranslucent(context,0);
        init();
    }

    private void init() {
        etName = (EditText)findViewById(R.id.et_name);
        etPwd = (EditText)findViewById(R.id.et_pwd);
        btLogin = (TextView)findViewById(R.id.bt_login);
        tv_register = (TextView)findViewById(R.id.tv_register);
        btLogin.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // if user click on login
            // check the user name and password first
            case R.id.bt_login:
                String name = etName.getText().toString();
                String password = etPwd.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(context, "please enter user name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(context, "Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                // compare the info in data base and return the login result
                BmobUser bu2 = new BmobUser();
                bu2.setUsername(name);
                bu2.setPassword(password);
                bu2.login(new SaveListener<BmobUser>() {

                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if (e == null) {
                            Toast.makeText(context, "login successfully", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                break;
            // if user clicks the register button
            case R.id.tv_register:

                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
        }
    }
}
