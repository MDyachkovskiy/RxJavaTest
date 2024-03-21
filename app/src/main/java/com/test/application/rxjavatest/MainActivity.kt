package com.test.application.rxjavatest

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private val disposables = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.et_edittext)

        val textChangeObservable = Observable.create { emitter ->
            editText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!emitter.isDisposed) {
                        emitter.onNext(p0.toString())
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }
            .debounce(3, TimeUnit.SECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())

        disposables.add(
            textChangeObservable.subscribe{ text ->
                Log.d("@@@", "Выводимый текст: $text")
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}