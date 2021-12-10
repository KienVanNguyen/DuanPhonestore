package com.kiennv.duanphonestore.User.MyRetrofit;



import com.kiennv.duanphonestore.User.Model.ServerRequest;
import com.kiennv.duanphonestore.User.Model.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {
    @POST("learn-login-register/")
    Call<ServerResponse> operation(@Body ServerRequest request);
}
