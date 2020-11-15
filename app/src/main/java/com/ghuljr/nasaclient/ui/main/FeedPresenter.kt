package com.ghuljr.nasaclient.ui.main

import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.repository.NasaRepository
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter
import com.ghuljr.nasaclient.ui.common.NetworkError
import com.ghuljr.nasaclient.ui.common.ResourceError
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class FeedPresenter(
    private val nasaRepository: NasaRepository
) : BasePresenter<FeedView>() {

    private val refreshApodSubject: BehaviorSubject<Unit> = BehaviorSubject.create()
    private val initApodSubject: BehaviorSubject<Unit> = BehaviorSubject.create()

    private val initApodObservable: Observable<ApodModel> = initApodSubject
        .subscribeOn(Schedulers.io())
        .flatMap { nasaRepository.getLatestApod() }
        .doOnNext { refreshApodSubject.onNext(Unit) }
        .take(1)
        .observeOn(AndroidSchedulers.mainThread())

    private val refreshApodObservable: Observable<Resource<ApodModel>> = refreshApodSubject
        .subscribeOn(Schedulers.io())
        .switchMap { nasaRepository.updateApod().startWith(Resource.Loading()) }
        .replay(1).refCount()

    private val refreshApodSuccessObservable: Observable<ApodModel> = refreshApodObservable
        .filter { it is Resource.Success && it.data != null }
        .map { it.data!! }
        .observeOn(AndroidSchedulers.mainThread())

    private val refreshApodLoadingObservable: Observable<Boolean> = refreshApodObservable
        .map { it is Resource.Loading }
        .observeOn(AndroidSchedulers.mainThread())

    private val refreshApodErrorObservable: Observable<ResourceError> = refreshApodObservable
        .filter { it is Resource.Error }
        .map { it.error ?: NetworkError.ResponseError }
        .observeOn(AndroidSchedulers.mainThread())

    override fun onViewAttached() {
        super.onViewAttached()

        disposable.set(CompositeDisposable(
            initApodObservable.subscribe { view?.diplayApod(it) },
            refreshApodSuccessObservable.subscribe { view?.diplayApod(it) },
            refreshApodErrorObservable.subscribe { view?.displayApodError(it) },
            refreshApodLoadingObservable.subscribe { /*TODO: make skeleton loader*/ }
        ))
    }

    fun refreshApod(): Unit = refreshApodSubject.onNext(Unit)
    fun initApod(): Unit = initApodSubject.onNext(Unit)


    companion object {
        private const val TAG = "FeedPresenter"
    }
}

