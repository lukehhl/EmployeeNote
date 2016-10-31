package com.example.administrator.employeenote.intface;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by GE11522 on 2016/10/21.
 */

public class PublicInterface {
    public interface FileUploadService { //retrofit上传文件接口
        @Multipart
        @POST("updMission.do")
        Call<ResponseBody> upload(@Part("eid") String eid,
                                  @Part("vdes") String vdes,
                                  @Part("vtime") String vtime,
                                  @Part("vsrc") RequestBody src,
                                  @Part MultipartBody.Part file,
                                  @Part("vsign") String vsign,
                                  @Part("vcustomer") String vcustomer);
    }
}
