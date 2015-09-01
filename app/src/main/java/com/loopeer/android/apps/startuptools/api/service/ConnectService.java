package com.loopeer.android.apps.startuptools.api.service;

import com.laputapp.http.Response;
import com.loopeer.android.apps.startuptools.model.Category;
import com.loopeer.android.apps.startuptools.model.Product;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by KorHsien on 2015/3/23.
 */
public interface ConnectService {

    @GET("/api/v1/categories")
    void getCategories(
            @Query("parent_id") int parentId,
            @Query("page") int page,
            @Query("page_size") int pageSize,
            Callback<Response<ArrayList<Category>>> callback
    );

    @GET("/api/v1/products")
    void getProducts(
            @Query("category_id") int categoryId,
            @Query("page") int page,
            @Query("page_size") int pageSize,
            Callback<Response<ArrayList<Product>>> callback
    );

    @GET("/api/v1/product")
    void getProduct(
            @Query("product_id") int productId,
            Callback<Response<Product>> callback
    );

    @POST("/api/v1/system/feedback")
    void sendFeedback(
            @Query("content") String content,
            @Query("contact") String contact,
            Callback<Response> callback
    );

    @POST("/api/v1/system/contribute")
    void contribute(
            @Query("title") String title,
            @Query("contact") String contact,
            Callback<Response> callback
    );
}
