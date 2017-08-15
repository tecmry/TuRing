package com.example.tecmry.turing.Bean;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;


public interface TuringChat {
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("openapi/api")
    Observable<RsChat> savedMessage(@Body RequestBody requestBody);
   }

