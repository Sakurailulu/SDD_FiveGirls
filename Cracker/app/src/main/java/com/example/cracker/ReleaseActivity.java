package com.example.cracker;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cracker.bean.Course;
import com.example.cracker.bean.Data;
import com.example.cracker.bean.User;
import com.example.cracker.utils.Utils;
import java.io.File;
import java.io.FileNotFoundException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

// upload new course material
public class ReleaseActivity extends BaseActivity {
    private EditText et_title;
    private TextView et_content;
    private ImageView iv_img;
    private BmobFile bmobFile;
    private boolean fileSuccess = false;
    private Course data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        data = (Course) getIntent().getSerializableExtra("data");
        onSetTitle("Upload");
        initView();
    }




    private void initView() {
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        iv_img = findViewById(R.id.iv_img);
        // if user click on upload image, will bring up a reminder window
        iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ReleaseActivity.this)
                        .setTitle("Tips")
                        .setMessage("upload image")
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getPhoto();
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
            }
        });
        Button send = findViewById(R.id.send);
        // if user submit the material
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if all contents are valid
                String title = et_title.getText().toString();
                String content = et_content.getText().toString();
                if (TextUtils.isEmpty(title)||TextUtils.isEmpty(content)){
                    onToast("Title cannot be empty");
                    return;
                }
                if (!fileSuccess){
                    onToast("Please upload an image");
                    return;
                }


                upLoad(title,content);
            }
        });

    }
    // upload the file to the database
    private void upLoad(String title, String content) {
        Data trend =  new Data();
        trend.setTitle(title);
        trend.setImg(bmobFile.getUrl());
        trend.setContent(content);
        trend.setCourse(data);
        trend.setUser(BmobUser.getCurrentUser(User.class));
        trend.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    onToast("upload successfully");
                    finish();
                }else{
                    onToast("upload failed");
                }
            }
        });
    }
    private void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 100);

    }

    // visit the local pictures
    @SuppressLint("SdCardPath")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    if (data != null) {
                        Uri uri_ = data.getData();
                        if (uri_ != null) {
                            try {
                                String img = Utils.getPath(ReleaseActivity.this, uri_);
                                updateImage(img);
                                Bitmap photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri_));
                                iv_img.setImageBitmap(photo);
                            } catch (FileNotFoundException e) {
                                onToast("Cannot Find File");
                                e.printStackTrace();
                            }
                        }
                    }
                    break;


                default:
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void updateImage(final String imageName) {
        bmobFile = new BmobFile(new File(imageName));
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){

                    fileSuccess = true;
                }else{
                }
            }
        });
    }
}
