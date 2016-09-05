package com.example.administrator.employeenote.intface;

import com.example.administrator.employeenote.entity.getHistory.HistoryPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/8/13.
 */
public interface histroyGetIF {
    @GET("gethistory")
    Call<HistoryPOJO> gethistroy(@Query("service_id") String serid,
                                 @Query("ak") String ak,
                                 @Query("mcode") String mcode,
                                 @Query("entity_name") String ename,
                                 @Query("start_time") String stime,
                                 @Query("end_time") String etime);
}
