package com.ghuljr.nasaclient.ui.splash

import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.repository.NasaRepository
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class SplashPresenter(
    private val nasaRepository: NasaRepository
) : BasePresenter<SplashView>() {

    private val updateApodListSubject: BehaviorSubject<Unit> = BehaviorSubject.create()

    private val isApodCachedObservable: Observable<Boolean> = nasaRepository
        .getApodList()
        .map { it.isNotEmpty() }

    private val updateApodListObservable: Observable<Resource<ApodModel>> = updateApodListSubject
        .flatMap { nasaRepository.updateApod().startWith(Resource.Loading()) }
        .replay(1).refCount()

    private val shouldLaunchAppObservable: Observable<Boolean> = updateApodListObservable
        .filter { it is Resource.Error }
        .flatMap { isApodCachedObservable }
        .replay(1).refCount()

    private val redirectToAppObservable: Observable<Unit> = updateApodListObservable
        .filter { it is Resource.Success }
        .map { Unit }
        .observeOn(AndroidSchedulers.mainThread())

    private val redirectToAppWithErrorObservable: Observable<Unit> = shouldLaunchAppObservable
        .filter { it }
        .map { Unit }
        .observeOn(AndroidSchedulers.mainThread())

    private val showApodErrorObservable: Observable<Unit> = shouldLaunchAppObservable
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
            redirectToAppWithErrorObservable.subscribe { view?.redirectToMainActivity() },
            showApodErrorObservable.subscribe { view?.displayErrorDialog() },
            isDataLoadingObservable.subscribe { view?.displayLoadingView(it) }
        ))
    }

    fun updateApod(): Unit = updateApodListSubject.onNext(Unit)

}