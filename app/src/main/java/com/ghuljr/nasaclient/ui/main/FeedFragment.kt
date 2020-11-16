package com.ghuljr.nasaclient.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.ghuljr.nasaclient.R
import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.ui.base.mvp.BaseView
import com.ghuljr.nasaclient.ui.base.mvp.MVPLifecycleObserver
import com.ghuljr.nasaclient.ui.base.mvp.RetainedState
import com.ghuljr.nasaclient.ui.common.NetworkError
import com.ghuljr.nasaclient.ui.common.ResourceError
import com.ghuljr.nasaclient.utils.getLifecycleObserver
import com.ghuljr.nasaclient.utils.loadImage
import com.ghuljr.nasaclient.utils.makeSnackbar
import kotlinx.android.synthetic.main.component_apod_header.view.*
import kotlinx.android.synthetic.main.fragment_feed.*
import org.koin.android.ext.android.inject

interface FeedView : BaseView<FeedPresenter> {
    fun diplayApod(apod: ApodModel)
    fun displayApodError(error: ResourceError)
    fun displayLoading(isLoading: Boolean)
    fun setApodArchiveList(apods: List<ApodModel>)
    fun setApodArchiveVisibility(isVisible: Boolean)
    fun openApodDetails(apod: ApodModel)
}

class FeedFragment : Fragment(), FeedView {

    private val feedPresenter: FeedPresenter by inject()
    private val retainedState: RetainedState by viewModels()
    private val lifecycleObserver: MVPLifecycleObserver<FeedView, FeedPresenter> by lazy { getLifecycleObserver() }

    private lateinit var apodAdapter: ApodAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    //TODO: make view more reactive
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apodAdapter = ApodAdapter { feedPresenter.openCurrentApodDetails() }

        apod_recyclerview.adapter = apodAdapter
        apod_recyclerview.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
                .apply { setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider_horizontal_list)!!) }
        )

        apodHeader.setOnClickListener { feedPresenter.openCurrentApodDetails() }

        viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
    }

    override fun diplayApod(apod: ApodModel) {
        with(apodHeader) {
            apodTitle.text = apod.title
            apodDate.text = apod.date
            apodHeaderImage.loadImage(apod.url)
        }
    }

    override fun displayApodError(error: ResourceError) {
        when(error) {
            is NetworkError.UpToDateError -> makeSnackbar(apodRoot, R.string.error_up_to_date, R.string.ok)
            else -> makeSnackbar(apodRoot, R.string.error_internet_connection, R.string.retry_label) {
                feedPresenter.refreshApod()
            }
        }
    }

    override fun displayLoading(isLoading: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setApodArchiveList(apods: List<ApodModel>) {
        apodAdapter.apods = apods
    }

    override fun setApodArchiveVisibility(isVisible: Boolean) {
        apod_archive.isVisible = isVisible
    }

    override fun openApodDetails(apod: ApodModel) {
        findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToApodDetailsFragment(apod.id))
    }

    override fun getPresenter(): FeedPresenter = feedPresenter
    override fun getState(): RetainedState = retainedState

    companion object {
        @JvmStatic
        fun newInstance() = FeedFragment()
    }
}
