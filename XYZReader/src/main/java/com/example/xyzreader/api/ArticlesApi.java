package com.example.xyzreader.api;

import com.example.xyzreader.model.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ArticlesApi {
    public static final String BASE_ENDPOINT = "https://dl.dropboxusercontent.com/u/231329/xyzreader_data/";

    @GET("data.json")
    public Call<List<Article>> getArticles();
}
