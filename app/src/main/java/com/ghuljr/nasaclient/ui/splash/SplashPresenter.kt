package com.ghuljr.nasaclient.ui.splash

import android.util.Log
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
        .share()
        .doOnNext { Log.i("SplashTest", "check1 - ${it.error}") }

    private val shouldLaunchAppObservable: Observable<Boolean> = updateApodListObservable
        .filter { it is Resource.Error }
        .flatMap { isApodCachedObservable }
        .share()
        .doOnNext { Log.i("SplashTest", "check2") }

    private val redirectToAppObservable: Observable<Unit> = updateApodListObservable
        .doOnNext { Log.i("SplashTest", "check3a") }
        .filter { it is Resource.Success }
        .doOnNext { Log.i("SplashTest", "check3b") }
        .map { Unit }
        .observeOn(AndroidSchedulers.mainThread())

    private val redirectToAppWithErrorObservable: Observable<Unit> = shouldLaunchAppObservable
        .doOnNext { Log.i("SplashTest", "check4a") }
        .filter { it }
        .doOnNext { Log.i("SplashTest", "check4a") }
        .map { Unit }
        .observeOn(AndroidSchedulers.mainThread())

    private val showApodErrorObservable: Observable<Unit> = shouldLaunchAppObservable
        .doOnNext { Log.i("SplashTest", "check5a") }
        .filter { !it }
        .doOnNext { Log.i("SplashTest", "check5b") }
        .map { Unit }
        .observeOn(AndroidSchedulers.mainThread())

    private val isDataLoadingObservable: Observable<Boolean> = updateApodListObservable
        .doOnNext { Log.i("SplashTest", "check6a") }
        .map { it is Resource.Loading }
        .doOnNext { Log.i("SplashTest", "check6b") }
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