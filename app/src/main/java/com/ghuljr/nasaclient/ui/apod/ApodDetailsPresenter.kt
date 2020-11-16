package com.ghuljr.nasaclient.ui.apod

import com.ghuljr.nasaclient.data.repository.NasaRepository
import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter

class ApodDetailsPresenter(
    private val nasaRepository: NasaRepository
) : BasePresenter<ApodDetailsView>() {


}