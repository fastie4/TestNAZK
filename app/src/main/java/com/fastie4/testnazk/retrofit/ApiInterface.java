package com.fastie4.testnazk.retrofit;

import com.fastie4.testnazk.pojo.Declaration;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("v1/declaration/")
    Observable<Declaration> getDeclarations(@Query("q") String query);
}
