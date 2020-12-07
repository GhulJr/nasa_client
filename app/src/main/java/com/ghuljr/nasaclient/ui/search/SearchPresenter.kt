package com.ghuljr.nasaclient.ui.search

import com.ghuljr.nasaclient.data.repository.NasaRepository
import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter

class SearchPresenter(
    private val nasaRepository: NasaRepository
): BasePresenter<SearchView>() {

}