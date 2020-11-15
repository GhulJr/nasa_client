package com.ghuljr.nasaclient.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.ghuljr.nasaclient.R
import com.ghuljr.nasaclient.ui.base.mvp.BaseView
import com.ghuljr.nasaclient.ui.base.mvp.MVPLifecycleObserver
import com.ghuljr.nasaclient.ui.base.mvp.RetainedState

import com.ghuljr.nasaclient.ui.main.MainActivity
import com.ghuljr.nasaclient.utils.getLifecycleObserver
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.ext.android.inject

interface SplashView : BaseView<SplashPresenter> {
    fun redirectToMainActivity()
    fun displayErrorDialog()
    fun displayLoadingView(isLoading: Boolean)
}

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
        AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog)
            .setTitle(R.string.error_network)
            .setMessage(R.string.error_internet_connection)
            .setNegativeButton(R.string.exit) { _, _ -> finish() }
            .setPositiveButton(R.string.retry_label) { _, _ -> splashPresenter.updateApod() }
            .setCancelable(false)
            .show()
    }

    override fun displayLoadingView(isLoading: Boolean) {
        activity_splash_loading_indicator.isVisible = isLoading
    }

    override fun getPresenter(): SplashPresenter = splashPresenter

    override fun getState(): RetainedState = retainedState
}
