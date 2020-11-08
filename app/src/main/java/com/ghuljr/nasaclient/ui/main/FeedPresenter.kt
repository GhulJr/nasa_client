package com.ghuljr.nasaclient.ui.main

import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.repository.NasaRepository
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class FeedPresenter(
    private val nasaRepository: NasaRepository
) : BasePresenter<FeedView>() {

    private val apodSubject: BehaviorSubject<Resource<ApodModel>> =
        BehaviorSubject.createDefault(Resource.Loading())

    private val apodSuccessObserver: Observable<ApodModel> = apodSubject
        .filter { it is Resource.Success && it.data != null }
        .map { it.data!! }
        .observeOn(Schedulers.io())


    override fun onViewAttached() {
        super.onViewAttached()

        disposable.set(CompositeDisposable(
            apodSuccessObserver.subscribe { view?.diplayApod(it) }
        ))
    }

    companion object {
        private const val TAG = "FeedPresenter"
    }
}

