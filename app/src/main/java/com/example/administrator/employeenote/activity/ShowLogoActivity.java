package com.example.administrator.employeenote.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.widget.LogoView;

public class ShowLogoActivity extends AppCompatActivity {
    LogoView logoview;
    public static int height;
    public static int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);//获取宽度高度
        height = dm.heightPixels;//记录宽度
        width = dm.widthPixels;//记录宽度
        logoview = new LogoView(this);
        setContentView(logoview);
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    public void gotoMainActivity() {
        Intent intent = new Intent(ShowLogoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
