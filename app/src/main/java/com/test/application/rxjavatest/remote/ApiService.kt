package com.test.application.rxjavatest.remote

import com.test.application.rxjavatest.model.Comment
import com.test.application.rxjavatest.model.Post
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("posts/{id}")
    fun getPosts(@Path("id") id: Int): Single<Post>

    @GET("comments/{id}")
    fun getComments(@Path("id") id: Int): Single<Comment>
}