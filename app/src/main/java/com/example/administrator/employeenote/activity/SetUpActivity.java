package com.example.administrator.employeenote.activity;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.utils.DataCleanManager;

public class SetUpActivity extends AppCompatActivity {
    private ImageView back;
    private Button ccache;

    private Handler mhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        initView();
    }

    private void initView() {

        mhandler = new Handler(Looper.getMainLooper());
        back = (ImageView) findViewById(R.id.back);
        ccache = (Button) findViewById(R.id.cleancache);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String cache = null;
        try {
            cache = DataCleanManager.getCacheSize(getApplicationContext().getCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ccache.setText("清除录音缓存  当前缓存: " + cache);

        ccache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanCache();
            }
        });
    }

    private void cleanCache() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                DataCleanManager.cleanInternalCache(getApplicationContext());
                String cache = null;

                try {
                    cache = DataCleanManager.getCacheSize(getApplicationContext().getCacheDir());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final String finalCache = cache;
                mhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ccache.setText("清除录音缓存  当前缓存: " + finalCache);
                    }
                });
            }
        }.start();
    }
}
