package com.test.application.rxjavatest.remote

import com.test.application.rxjavatest.model.Post
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("posts/{id}")
    fun getPosts(@Path("id") postId: Int): Observable<Post>
}