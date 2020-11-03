package com.ghuljr.nasaclient.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ghuljr.nasaclient.R
import com.ghuljr.nasaclient.ui.base.mvp.BaseView

class FeedFragment : Fragment(), FeedView {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun getPresenter(): FeedPresenter {
        TODO("Not implemented yet")
    }

    companion object {
        @JvmStatic
        fun newInstance() = FeedFragment()
    }

}

interface FeedView : BaseView<FeedPresenter> {

}