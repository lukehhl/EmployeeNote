package com.example.administrator.employeenote.common;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2016/8/13.
 */
public class test {


    public static void main(String arges[]){
        init_retrofit2();
    }

    interface simpleGet {
        @GET("/")
        Call<ResponseBody> getlastRespone();
    }
    private static void init_retrofit2() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.map.baidu.com/trace/v2/entity/list/")
                .build();
       simpleGet simpleGet =  retrofit.create(simpleGet.class);
        Call<ResponseBody> call = simpleGet.getlastRespone();
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                System.out.println(response.body().toString());
                }else System.out.println("error");

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(call.toString());

            }
        });
    }



}
