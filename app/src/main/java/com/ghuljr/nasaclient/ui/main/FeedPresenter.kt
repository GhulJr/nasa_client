package com.ghuljr.nasaclient.ui.main

import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.repository.NasaRepository
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class FeedPresenter(
    private val nasaRepository: NasaRepository
) : BasePresenter<FeedView>() {

    private val refreshApodSubject: BehaviorSubject<Unit> = BehaviorSubject.create()
    private val initApodSubject: BehaviorSubject<Unit> = BehaviorSubject.create()

    private val initApodObservable: Observable<ApodModel> = initApodSubject
        .flatMap { nasaRepository.getLatestApod() }
        .take(1)
        .observeOn(AndroidSchedulers.mainThread())

    private val refreshApodObservable: Observable<Resource<ApodModel>> = refreshApodSubject
        .flatMap { initApodObservable.takeLast(1) }
        .switchMap { nasaRepository.fetchApod().toObservable().startWith(Resource.Loading()) }
        .repeat(1).share()

    private val refreshApodSuccessObservable: Observable<ApodModel> = refreshApodObservable
        .filter { it is Resource.Success && it.data != null }
        .map { it.data!! }
        .observeOn(AndroidSchedulers.mainThread())

    private val refreshApodLoadingObservable: Observable<Boolean> = refreshApodObservable
        .map { it is Resource.Loading }
        .observeOn(AndroidSchedulers.mainThread())

    private val refreshApodErrorObservable: Observable<Unit> = refreshApodObservable
        .filter { it is Resource.Error }
        .map { Unit }
        .observeOn(AndroidSchedulers.mainThread())


    override fun onViewAttached() {
        super.onViewAttached()

        disposable.set(CompositeDisposable(
            initApodObservable.subscribe { view?.diplayApod(it) },
            refreshApodSuccessObservable.subscribe { view?.diplayApod(it) },
            refreshApodErrorObservable.subscribe { /* TODO: display snackbar */ },
            refreshApodLoadingObservable.subscribe { /*TODO: make skeleton loader*/ }
        ))
    }

    fun refreshApod(): Unit = refreshApodSubject.onNext(Unit)
    fun initApod(): Unit = initApodSubject.onNext(Unit)


    companion object {
        private const val TAG = "FeedPresenter"
    }
}

