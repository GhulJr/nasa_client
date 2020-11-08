package com.ghuljr.nasaclient.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ghuljr.nasaclient.R
import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.ui.base.mvp.BaseView
import org.koin.experimental.property.inject

class FeedFragment : Fragment(), FeedView {

    private val feedPresenter: FeedPresenter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun diplayApod(apod: ApodModel) {

    }

    override fun getPresenter(): FeedPresenter = feedPresenter

    companion object {
        @JvmStatic
        fun newInstance() = FeedFragment()
    }

}

interface FeedView : BaseView<FeedPresenter> {
    fun diplayApod(apod: ApodModel)
}