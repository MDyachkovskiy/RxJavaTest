package com.test.application.rxjavatest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.application.rxjavatest.databinding.ItemViewBinding
import com.test.application.rxjavatest.model.Post
import io.reactivex.rxjava3.subjects.PublishSubject

class PostsAdapter : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    private val posts = mutableListOf<Post>()
    val clickSubject: PublishSubject<Int> = PublishSubject.create()

    fun addPost(post: Post) {
        posts.add(post)
        notifyItemInserted(posts.size - 1)
    }

    class ViewHolder(val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            tvTitle.text = "Заголовок: $position"
            tvBody.text = "Текст: $position"

            root.setOnClickListener { clickSubject.onNext(position) }
        }
    }
}