package com.ghuljr.nasaclient.ui.apod

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.ghuljr.nasaclient.R
import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.ui.base.mvp.BaseView
import com.ghuljr.nasaclient.ui.base.mvp.MVPLifecycleObserver
import com.ghuljr.nasaclient.ui.base.mvp.RetainedState
import com.ghuljr.nasaclient.utils.getLifecycleObserver
import com.ghuljr.nasaclient.utils.loadImage
import kotlinx.android.synthetic.main.fragment_apod_details.*
import kotlinx.android.synthetic.main.item_apod.*
import org.koin.android.ext.android.inject

interface ApodDetailsView : BaseView<ApodDetailsPresenter> {
    fun displayApod(apod: ApodModel)
}

class ApodDetailsFragment : Fragment(), ApodDetailsView {

    private val apodDetailsPresenter: ApodDetailsPresenter by inject()
    private val retainedState: RetainedState by viewModels()
    private val args: ApodDetailsFragmentArgs by navArgs()
    private val lifecycleObserver: MVPLifecycleObserver<ApodDetailsView, ApodDetailsPresenter> by lazy { getLifecycleObserver() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_apod_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        NavigationUI.setupWithNavController(toolbar, findNavController())
        toolbar.title = ""

        apodDetailsPresenter.getApodDetails(args.apodId)
    }

    override fun displayApod(apod: ApodModel) {
        apod_image.loadImage(apod.url)
        apodTitle.text = apod.title
        apod_details_date.text = apod.date
        apod_explanation.text = apod.explanation
    }


    override fun getPresenter(): ApodDetailsPresenter = apodDetailsPresenter
    override fun getState(): RetainedState = retainedState

    companion object {
        fun newInstance() = ApodDetailsFragment()
    }
}