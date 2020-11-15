package com.ghuljr.nasaclient.ui.splash

import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.repository.NasaRepository
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class SplashPresenter(
    private val nasaRepository: NasaRepository
) : BasePresenter<SplashView>() {

    private val updateApodListSubject: BehaviorSubject<Unit> = BehaviorSubject.create()

    private val isDataCachedObservable: Observable<Boolean> = updateApodListSubject
        .subscribeOn(Schedulers.io())
        .flatMap { nasaRepository.getApodList() }
        .map { it.isNotEmpty() }

    private val updateApodListObservable: Observable<Resource<ApodModel>> = isDataCachedObservable
        .filter { !it }
        .flatMap { nasaRepository.fetchApod().toObservable().startWith(Resource.Loading()) }
        .doOnNext { nasaRepository.i}
        .replay(1).refCount()

    private val redirectToAppObservable: Observable<Unit> = isDataCachedObservable
        .filter { it }
        .map { Unit }
        .observeOn(AndroidSchedulers.mainThread())

    private val showApodErrorObservable: Observable<Unit> = isDataCachedObservable
        .filter { !it }
        .map { Unit }
        .observeOn(AndroidSchedulers.mainThread())

    private val isDataLoadingObservable: Observable<Boolean> = updateApodListObservable
        .map { it is Resource.Loading }
        .observeOn(AndroidSchedulers.mainThread())

    override fun onViewAttached() {
        super.onViewAttached()
        disposable.set(CompositeDisposable(
            redirectToAppObservable.subscribe { view?.redirectToMainActivity() },
            showApodErrorObservable.subscribe { view?.displayErrorDialog() },
            isDataLoadingObservable.subscribe { view?.displayLoadingView(it) }
        ))
    }

    fun updateApod(): Unit = updateApodListSubject.onNext(Unit)

}