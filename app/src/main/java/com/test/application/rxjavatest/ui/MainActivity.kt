package com.test.application.rxjavatest.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.test.application.rxjavatest.R
import com.test.application.rxjavatest.model.Comment
import com.test.application.rxjavatest.model.Post
import com.test.application.rxjavatest.remote.RetrofitService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()
    private lateinit var postsObservable: Observable<List<Post>>
    private lateinit var commentsObservable: Observable<List<Comment>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupPostsObservables()
        setupCommentsObservables()
        setupFinalData()

    }

    private fun setupFinalData() {
        disposables.add(
            Observable.zip(
                postsObservable,
                commentsObservable
            ) { posts, comments ->
                Pair(posts, comments)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ (posts, comments) ->
                    Log.d("@@@", "Успешно загруженные данные" +
                            "Количество постов: ${posts.size}, Количество комментариев: ${comments.size}")
                }, { error ->
                    Log.d("@@@", "Ошибка при загрузке данных", error)
                })
        )
    }

    private fun setupCommentsObservables() {
       commentsObservable = Observable.range(1, 5)
           .flatMapSingle { id ->
               RetrofitService.apiService.getComments(id)
                   .subscribeOn(Schedulers.io())
           }
           .toList()
           .toObservable()
    }

    private fun setupPostsObservables() {
        postsObservable = Observable.range(1, 5)
            .flatMapSingle { id ->
                RetrofitService.apiService.getPosts(id)
                    .subscribeOn(Schedulers.io())
            }
            .toList()
            .toObservable()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}