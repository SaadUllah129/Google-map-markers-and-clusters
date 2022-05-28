package com.java.testinggoogleapp.listeners;

import com.google.gson.JsonObject;
import com.java.testinggoogleapp.model.Post;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface JSONPlaceholder {
    @FormUrlEncoded
    //TODO: Insert your page url hear
//    Note: Modify this post query according to your API
    @POST("")
    Call<JsonObject> getPost(@Field("searchText") String searchText, @Field("apiKey") String ApiKey);
}
