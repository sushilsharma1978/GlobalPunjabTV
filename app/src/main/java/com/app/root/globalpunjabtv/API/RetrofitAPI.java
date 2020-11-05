package com.app.root.globalpunjabtv.API;

import com.app.root.globalpunjabtv.models.PostGetSet;
import com.app.root.globalpunjabtv.models.SinglePostGetSet;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface RetrofitAPI {
    @GET("cat.php")
    Call<JsonElement> getCategory();

    @GET("post.php")
    Call<PostGetSet> getPosts();

    @GET("singlepost.php")
    Call<SinglePostGetSet> getSinglePost(@Path("postid") Integer postid, @Url  String singleposturl);

}
