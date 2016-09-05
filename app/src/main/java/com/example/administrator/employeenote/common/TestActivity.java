package com.example.administrator.employeenote.common;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.employeenote.R;

public class TestActivity extends Activity {
    private TextView mtext,mttext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mtext = (TextView) findViewById(R.id.ttext);
        mttext = (TextView) findViewById(R.id.tttext);
//        init_retrofit2();
    }

//
//
//    private void init_retrofit2() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://api.map.baidu.com/trace/v2/track/gethistory")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        histroyGetIF histroyGetIF = retrofit.create(histroyGetIF.class);
//        Call<SearchPOJO> call = histroyGetIF.gethistroy("122424",
//                "kAq1Mi2NFQT3WUFs7tUQWhjGCEmG38rF",
//                "23:3B:2C:8F:09:90:5D:43:4C:CD:1C:C0:E6:0F:3C:84:01:B0:FC:55;com.example.administrator.employeenote",
//                "mycar");
//        call.enqueue(new Callback<SearchPOJO>() {
//            @Override
//            public void onResponse(Call<SearchPOJO> call, Response<SearchPOJO> response) {
//                if (response.isSuccessful()) {
//                    System.out.println(response.body().getStatus());
//
//                        SearchPOJO searchPOJO = response.body();
//                        mtext.setText(String.valueOf(response.body().getStatus()));
//                        mttext.setText(Long.toString(new Date().getTime()/1000));
//
//
//
//                }else
//                    System.out.println("error");
//
//            }
//
//            @Override
//            public void onFailure(Call<SearchPOJO> call, Throwable t) {
//                Log.d("Em",t.toString());
//            }
//
//
//        });
//    }
}
