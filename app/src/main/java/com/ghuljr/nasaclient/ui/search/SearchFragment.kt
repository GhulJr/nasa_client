package com.ghuljr.nasaclient.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.ghuljr.nasaclient.R
import com.ghuljr.nasaclient.ui.base.mvp.BaseView
import com.ghuljr.nasaclient.ui.base.mvp.MVPLifecycleObserver
import com.ghuljr.nasaclient.ui.base.mvp.RetainedState
import com.ghuljr.nasaclient.utils.getLifecycleObserver
import kotlinx.android.synthetic.main.fragment_apod_details.toolbar
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.ext.android.inject

interface SearchView : BaseView<SearchPresenter> {

}

class SearchFragment : Fragment(), SearchView {

    private val searchPresenter: SearchPresenter by inject()
    private val retainedState: RetainedState by viewModels()
    private val lifecycleObserver: MVPLifecycleObserver<SearchView, SearchPresenter> by lazy { getLifecycleObserver() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        NavigationUI.setupWithNavController(toolbar, findNavController())
        toolbar.title = ""

        //TODO: do it more reactive.
        fragment_search_edit_text.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = fragment_search_edit_text.text.toString()
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(query))
                true
            } else false
        }
    }

    override fun getPresenter(): SearchPresenter = searchPresenter
    override fun getState(): RetainedState = retainedState
}