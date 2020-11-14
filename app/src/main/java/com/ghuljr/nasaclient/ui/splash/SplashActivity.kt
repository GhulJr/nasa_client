package com.ghuljr.nasaclient.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.ghuljr.nasaclient.R
import com.ghuljr.nasaclient.ui.base.mvp.BaseView
import com.ghuljr.nasaclient.ui.base.mvp.MVPLifecycleObserver
import com.ghuljr.nasaclient.ui.base.mvp.RetainedState

import com.ghuljr.nasaclient.ui.main.MainActivity
import com.ghuljr.nasaclient.utils.getLifecycleObserver
import org.koin.android.ext.android.inject

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
        finish()
    }

    override fun displayErrorDialog() {
        AlertDialog.Builder(this, android.R.style.Theme_Dialog)
            .setTitle(R.string.network_error)
            .setMessage(R.string.network_error_message)
            .setNegativeButton(R.string.exit) { _, _ -> finish() }
            .setPositiveButton(R.string.retry_label) { _, _ -> splashPresenter.updateApod() }
            .setCancelable(false)
            .show()
    }

    override fun getPresenter(): SplashPresenter = splashPresenter

    override fun getState(): RetainedState = retainedState
}

interface SplashView : BaseView<SplashPresenter> {
    fun redirectToMainActivity()
    fun displayErrorDialog()
}

