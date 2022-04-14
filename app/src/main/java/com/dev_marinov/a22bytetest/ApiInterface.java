package com.dev_marinov.a22bytetest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    String url = "https://newsapi.org/v2/";

    @GET("top-headlines")
    Call<News> getNews(
            @Query("country") String country,
            @Query("pageSize") int pageSize,
            @Query("apiKey") String apiKey);

    @GET("top-headlines")
    Call<News> getCategoryNews(
            @Query("country") String country,
            @Query("category") String category,
            @Query("pageSize") int pageSize,
            @Query("apiKey") String apiKey);

    @GET("everything")
    Call<News> getSearchNews(
            @Query("q") String q,
            @Query("apiKey") String apiKey);




}
