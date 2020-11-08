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
import org.koin.android.ext.android.inject

interface FeedView : BaseView<FeedPresenter> {
    fun diplayApod(apod: ApodModel)
}

class FeedFragment : Fragment(), FeedView {

    private val feedPresenter: FeedPresenter by inject()
    private val retainedState: RetainedState by viewModels()
    private lateinit var mvpLifecycleObserver: MVPLifecycleObserver<FeedView, FeedPresenter>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedPresenter.fetchApod()
    }

    override fun diplayApod(apod: ApodModel) {

    }

    override fun getPresenter(): FeedPresenter = feedPresenter

    companion object {
        @JvmStatic
        fun newInstance() = FeedFragment()
    }

}