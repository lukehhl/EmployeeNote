package com.example.administrator.employeenote.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.employeenote.R;

public class LoginActivity extends AppCompatActivity {
    private android.support.design.widget.TextInputLayout meid, mepassword;
    private Button mlogin;
    private CheckBox mrembpw;
    private View focusView;
    private SharedPreferences sharedPreferences;

    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }


    private void initView() {

        sharedPreferences = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        mHandler = new Handler(Looper.getMainLooper());

        meid = (TextInputLayout) findViewById(R.id.eid);
        mepassword = (TextInputLayout) findViewById(R.id.epassword);
        mrembpw = (CheckBox) findViewById(R.id.rembpw);
        mlogin = (Button) findViewById(R.id.login);


        Boolean ischecked = sharedPreferences.getBoolean("isrembpw", false);
        if (ischecked) {
            String id = sharedPreferences.getString("id", null);
            String password = sharedPreferences.getString("password", null);
            meid.getEditText().setText(id);
            mepassword.getEditText().setText(password);
            mrembpw.setChecked(ischecked);
        }


        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        meid.setError(null);
        mepassword.setError(null);
        String id = meid.getEditText().getText().toString();
        String password = mepassword.getEditText().getText().toString();
        boolean cancel = false;

        if (TextUtils.isEmpty(id)) {
            meid.setError("请输入工号");
            focusView = meid;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mepassword.setError("请输入密码");
            focusView = mepassword;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            // TODO 网络连接OA方法

            if ("1".equals(id) && "1".equals(password)) {//登录成功
                Toast.makeText(LoginActivity.this, "succeed", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor et = sharedPreferences.edit();
                if (mrembpw.isChecked()) {//是否记住密码
                    et.putBoolean("isrembpw", true);
                    et.putString("id", id);
                    et.putString("password", password);
                } else et.clear();
                et.commit();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                mepassword.setError("密码错误");
                focusView = mepassword;
                focusView.requestFocus();
                cancel = true;
            }

        }
    }



}
