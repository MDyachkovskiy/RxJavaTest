package com.test.application.rxjavatest.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.application.rxjavatest.adapter.PostsAdapter
import com.test.application.rxjavatest.databinding.ActivityMainBinding
import com.test.application.rxjavatest.remote_data.RetrofitService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val postsAdapter = PostsAdapter()
    private val disposables = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        fetchPosts()
    }

    private fun fetchPosts() {
        disposables.add(
            Observable.range(1,10)
                .flatMap { id ->
                    RetrofitService.apiService.getPostById(id)
                        .subscribeOn(Schedulers.io())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ post ->
                    postsAdapter.addPost(post)
                }, { error ->
                    Toast.makeText(this, "Ошибка при получении данных: $error",
                        Toast.LENGTH_SHORT)
                        .show()
                })
        )
    }

    private fun setupRecyclerView() {
        binding.rvMain.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = postsAdapter
        }
        disposables.add(
            postsAdapter.clickSubject
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe{ position ->
                    Toast.makeText(this@MainActivity, "Нажатие на позицию: $position",
                        Toast.LENGTH_SHORT)
                        .show()
                }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}