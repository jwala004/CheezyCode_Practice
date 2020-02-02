package com.example.cheezycode;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class BloggerAPI {

//    private static final String key = "AIzaSyC-fOjjG3BrbjqIb4FJAkh-QxIGm85ijEE";
//    private static final String url = "https://www.googleapis.com/blogger/v3/blogs/7578200398807454537/posts/";

//    made these fields public
    public static final String key = "AIzaSyC-fOjjG3BrbjqIb4FJAkh-QxIGm85ijEE";
    public static final String url = "https://www.googleapis.com/blogger/v3/blogs/7578200398807454537/posts/";

    public static PostService postService = null;

    public static PostService getService()
    {
        if(postService == null)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            postService = retrofit.create(PostService.class);
        }
        return postService;
    }

//    need to recheck

    public interface PostService {
//        @GET("?key="+key)
//        Call<PostList> getPostList();

//        commented the code above, it was for earlier thing
//        Now adding the code for Infinite RecyclerView
        @GET
        Call<PostList> getPostList(@Url String url);
    }


}
