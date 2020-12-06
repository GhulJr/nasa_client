package com.ghuljr.nasaclient.ui.search.search_result

import android.os.Bundle
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.ghuljr.nasaclient.R
import com.ghuljr.nasaclient.ui.base.mvp.BaseView
import com.ghuljr.nasaclient.ui.base.mvp.MVPLifecycleObserver
import com.ghuljr.nasaclient.ui.base.mvp.RetainedState
import com.ghuljr.nasaclient.utils.getLifecycleObserver
import org.koin.android.ext.android.inject
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ghuljr.nasaclient.data.model.NasaMediaModel
import kotlinx.android.synthetic.main.fragment_apod_details.toolbar
import kotlinx.android.synthetic.main.fragment_search_result.*

interface SearchResultView : BaseView<SearchResultPresenter> {
    fun setLoadingViews(isLoading: Boolean)
    fun displayNoResultsView(isEmptyResult: Boolean)
    fun displaySearchResult(items: List<NasaMediaModel>)
}

class SearchResultFragment : Fragment(), SearchResultView {

    private val searchResultPresenter: SearchResultPresenter by inject()
    private val retainedState: RetainedState by viewModels()
    private val lifecycleObserver: MVPLifecycleObserver<SearchResultView, SearchResultPresenter> by lazy { getLifecycleObserver() }
    private val navArgs: SearchResultFragmentArgs by navArgs()

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
        toolbar.title = "#${navArgs.searchQuery}"
        app_bar_layout.setExpanded(true, true)
    }

    override fun setLoadingViews(isLoading: Boolean) {
        fragment_search_result_loading_indicator.isVisible = isLoading
    }

    override fun displayNoResultsView(isEmptyResult: Boolean) {
        TODO("Not yet implemented")
    }

    override fun displaySearchResult(items: List<NasaMediaModel>) {
        TODO("Not yet implemented")
    }

    override fun getPresenter(): SearchResultPresenter = searchResultPresenter
    override fun getState(): RetainedState = retainedState
}