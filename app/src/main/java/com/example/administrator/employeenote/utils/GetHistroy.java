package com.example.administrator.employeenote.utils;

import android.util.Log;

import com.example.administrator.employeenote.entity.getHistory.HistoryPOJO;
import com.example.administrator.employeenote.intface.histroyGetIF;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/8/15.
 */
public class GetHistroy {
    private HistoryPOJO historyPOJO;



    public HistoryPOJO getHistory() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.map.baidu.com/trace/v2/track/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        histroyGetIF histroyGetIF = retrofit.create(histroyGetIF.class);
        Call<HistoryPOJO> call = histroyGetIF.gethistroy("122424",
                "kAq1Mi2NFQT3WUFs7tUQWhjGCEmG38rF",
                "23:3B:2C:8F:09:90:5D:43:4C:CD:1C:C0:E6:0F:3C:84:01:B0:FC:55;com.example.administrator.employeenote",
                "mycar",
                "1470181134",
                "1470181140");

        call.enqueue(new Callback<HistoryPOJO>() {
            @Override
            public void onResponse(Call<HistoryPOJO> call, Response<HistoryPOJO> response) {
                if (response.isSuccessful()) {
                    historyPOJO = response.body();
//                    System.out.println(historyPOJO.getEntity_name());
                    Log.d("Ename", historyPOJO.getEntity_name());
                } else
                    System.out.println("error");

            }

            @Override
            public void onFailure(Call<HistoryPOJO> call, Throwable t) {
                Log.d("error", t.toString());
            }


        });

        return historyPOJO;
    }
}
