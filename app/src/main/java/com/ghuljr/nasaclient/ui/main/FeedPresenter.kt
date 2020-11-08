package com.ghuljr.nasaclient.ui.main

import android.util.Log
import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.repository.NasaRepository
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter
import io.objectbox.android.AndroidScheduler
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class FeedPresenter(
    private val nasaRepository: NasaRepository
) : BasePresenter<FeedView>() {

    private val fetchApodSubject: BehaviorSubject<Unit> = BehaviorSubject.create()

    private val apodObservable: Observable<Resource<ApodModel>> = fetchApodSubject
        .switchMap { nasaRepository.fetchApod().toObservable() }
        .repeat(1).share()

    private val apodSuccessObserver: Observable<ApodModel> = apodObservable
        .filter { it is Resource.Success && it.data != null }
        .map { it.data!! }
        .observeOn(AndroidSchedulers.mainThread())

    private val apodErrorObservable: Observable<Unit> = apodObservable
        .filter { it is Resource.Error }
        .map { Unit }
        .observeOn(Schedulers.io())


    override fun onViewAttached() {
        super.onViewAttached()

        disposable.set(CompositeDisposable(
            apodSuccessObserver.subscribe { view?.diplayApod(it) },
            apodErrorObservable.subscribe { view }
        ))

    }

    fun fetchApod(): Unit = fetchApodSubject.onNext(Unit)


    companion object {
        private const val TAG = "FeedPresenter"
    }
}

