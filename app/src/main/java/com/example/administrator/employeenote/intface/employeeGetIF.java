package com.example.administrator.employeenote.intface;

import com.example.administrator.employeenote.entity.Employee.EmployeeData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by GE11522 on 2016/9/27.
 */

public interface employeeGetIF {
    @GET("getEmployee")
    Call<EmployeeData> getEmployee(@Query("id") String id,
                                   @Query("method") String method
    );
}
