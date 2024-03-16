package com.test.application.rxjavatest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.rxjava3.observables.ConnectableObservable
import io.reactivex.rxjava3.subjects.PublishSubject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testPublishSubjectOne()
        testPublishSubjectTwo()

    }

    private fun testPublishSubjectTwo() {
        val subject = PublishSubject.create<String>()
        val connectableObservable: ConnectableObservable<String> = subject.replay()

        subject.onNext("1")
        subject.onNext("2")
        subject.onNext("3")

        connectableObservable.connect()

        connectableObservable.subscribe { Log.d("@@@", it) }
    }

    private fun testPublishSubjectOne() {
        val subject = PublishSubject.create<String>()
        subject.subscribe { Log.d("@@@", it) }

        subject.onNext("1")
        subject.onNext("2")
        subject.onNext("3")
    }
}