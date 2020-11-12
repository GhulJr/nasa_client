package com.ghuljr.nasaclient.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ghuljr.nasaclient.R
import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.ui.base.mvp.BaseView
import com.ghuljr.nasaclient.ui.base.mvp.MVPLifecycleObserver
import com.ghuljr.nasaclient.ui.base.mvp.RetainedState
import com.ghuljr.nasaclient.utils.getLifecycleObserver
import com.ghuljr.nasaclient.utils.loadImage
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.component_apod_header.view.*
import kotlinx.android.synthetic.main.fragment_feed.*
import org.koin.android.ext.android.inject

interface FeedView : BaseView<FeedPresenter> {
    fun diplayApod(apod: ApodModel)
    fun displayApodError()
    fun displayLoading(isLoading: Boolean)
}

class FeedFragment : Fragment(), FeedView {

    private val feedPresenter: FeedPresenter by inject()
    private val retainedState: RetainedState by viewModels()
    private val lifecycleObserver: MVPLifecycleObserver<FeedView, FeedPresenter> by lazy { getLifecycleObserver() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        feedPresenter.initApod()
    }

    override fun diplayApod(apod: ApodModel) {
        with(apodHeader) {
            apodTitle.text = apod.title
            apodDate.text = apod.date
            apodHeaderImage.loadImage(apod.url)
        }
    }

    override fun displayApodError() {
        Snackbar.make(apodRoot, R.string.apod_fetch_error, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.retry) { feedPresenter.refreshApod() }
    }

    override fun displayLoading(isLoading: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getPresenter(): FeedPresenter = feedPresenter
    override fun getState(): RetainedState = retainedState

    companion object {
        @JvmStatic
        fun newInstance() = FeedFragment()
    }
}