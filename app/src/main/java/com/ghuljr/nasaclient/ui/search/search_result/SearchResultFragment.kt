package com.ghuljr.nasaclient.ui.search.search_result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.ghuljr.nasaclient.R
import com.ghuljr.nasaclient.ui.base.mvp.BaseView
import com.ghuljr.nasaclient.ui.base.mvp.MVPLifecycleObserver
import com.ghuljr.nasaclient.ui.base.mvp.RetainedState
import com.ghuljr.nasaclient.utils.getLifecycleObserver
import kotlinx.android.synthetic.main.fragment_apod_details.*
import org.koin.android.ext.android.inject
import androidx.fragment.app.viewModels

interface SearchResultView : BaseView<SearchResultPresenter> {

}

class SearchResultFragment : Fragment(), SearchResultView {

    private val searchResultPresenter: SearchResultPresenter by inject()
    private val retainedState: RetainedState by viewModels()
    private val lifecycleObserver: MVPLifecycleObserver<SearchResultView, SearchResultPresenter> by lazy { getLifecycleObserver() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        NavigationUI.setupWithNavController(toolbar, findNavController())
        toolbar.title = "#apollo"

    }

    override fun getPresenter(): SearchResultPresenter = searchResultPresenter
    override fun getState(): RetainedState = retainedState
}