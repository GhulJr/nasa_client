package com.ghuljr.nasaclient.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ghuljr.nasaclient.R
import com.ghuljr.nasaclient.ui.main.MainActivity
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Observable.just(Intent(this, MainActivity::class.java))
            .delay(1L, TimeUnit.SECONDS)
            .subscribe {
                startActivity(it)
                finish()
            }

        //TODO: fetch apod data
        //TODO: When you scroll top bar is dissapearing and fab is appearing
    }
}
