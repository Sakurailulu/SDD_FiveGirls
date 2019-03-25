package com.example.cracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cracker.fragment.HomeFragment;
import com.example.cracker.fragment.SettingFragment;


public class MainActivity extends BaseActivity implements View.OnClickListener {



    private MainActivity context;
    private HomeFragment homeFragment;
    private SettingFragment settingFragment;
    private Fragment[] fragments;
    private RelativeLayout[] TAB;
    private ImageView[] IMAGE;
    private int index;
    private int currentTabIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        context = this;
        initView();
    }




    private void initView() {

        if (null == homeFragment) {
            homeFragment = new HomeFragment();
        }

        if (null == settingFragment) {
            settingFragment = new SettingFragment();
        }
        fragments = new Fragment[]{homeFragment,
                settingFragment};
        TAB = new RelativeLayout[2];
        IMAGE = new ImageView[2];
        TAB[0] = findViewById(R.id.rl_home);
        TAB[1] =  findViewById(R.id.rl_me);

        IMAGE[0] = findViewById(R.id.iv_home);
        IMAGE[1] = findViewById(R.id.ib_me);
        IMAGE[0].setSelected(true);
        TAB[0].setOnClickListener(this);
        TAB[1].setOnClickListener(this);
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homeFragment, "home")
                .add(R.id.fragment_container, settingFragment, "set")
                .hide(settingFragment)
                .show(homeFragment).commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_home:
                index = 0;
                break;
            case R.id.rl_me:
                index = 1;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        IMAGE[currentTabIndex].setSelected(false);
        IMAGE[index].setSelected(true);
        currentTabIndex = index;
    }
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
