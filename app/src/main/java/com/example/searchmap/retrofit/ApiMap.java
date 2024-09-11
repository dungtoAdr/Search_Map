package com.example.searchmap.retrofit;

import com.example.searchmap.model.Maps;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiMap {
    @GET("geocode")
    Observable<Maps> searchAddress(@Query("q") String query, @Query("apiKey") String apiKey);
}
