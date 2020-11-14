package com.ghuljr.nasaclient.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.ghuljr.nasaclient.R
import com.ghuljr.nasaclient.ui.base.mvp.BaseView
import com.ghuljr.nasaclient.ui.base.mvp.MVPLifecycleObserver
import com.ghuljr.nasaclient.ui.base.mvp.RetainedState
import com.ghuljr.nasaclient.ui.main.FeedPresenter
import com.ghuljr.nasaclient.ui.main.FeedView
import com.ghuljr.nasaclient.ui.main.MainActivity
import com.ghuljr.nasaclient.utils.getLifecycleObserver
import io.reactivex.Observable
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity(), SplashView {

    private val splashPresenter: SplashPresenter by inject()
    private val retainedState: RetainedState by viewModels()
    private val lifecycleObserver: MVPLifecycleObserver<SplashView, SplashPresenter> by lazy { getLifecycleObserver() }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        this.lifecycle.addObserver(lifecycleObserver)

        splashPresenter.updateApod()
    }

    override fun redirectToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun displayErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.network_error)
            .setMessage(R.string.network_error_message)
            .setNegativeButton(R.string.exit) { _, _ -> finish() }
            .setPositiveButton(R.string.retry) { _, _ -> splashPresenter.updateApod() }
            .show()
    }

    override fun getPresenter(): SplashPresenter = splashPresenter

    override fun getState(): RetainedState = retainedState
}

interface SplashView : BaseView<SplashPresenter> {
    fun redirectToMainActivity()
    fun displayErrorDialog()
}

