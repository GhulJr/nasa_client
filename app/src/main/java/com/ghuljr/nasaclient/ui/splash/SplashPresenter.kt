package com.ghuljr.nasaclient.ui.splash

import com.ghuljr.nasaclient.data.repository.NasaRepository
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class SplashPresenter(
    private val nasaRepository: NasaRepository
) : BasePresenter<SplashView>() {

    private val updateApodListSubject: PublishSubject<Unit> = PublishSubject.create()

    private val updateApodListObservable: Observable<Resource<Void>> = updateApodListSubject
        .flatMap { nasaRepository.updateApodList() }

    private val redirectToAppObservable: Observable<Unit> = updateApodListObservable
        .filter { it is Resource.Success }
        .map { Unit }
        .observeOn(AndroidSchedulers.mainThread())

    private val showApodErrorObservable: Observable<Unit> = updateApodListObservable
        .filter { it is Resource.Error }
        .map { Unit }
        .observeOn(AndroidSchedulers.mainThread())

    override fun onViewAttached() {
        super.onViewAttached()
        disposable.set(CompositeDisposable(
            redirectToAppObservable.subscribe { view?.redirectToMainActivity() },
            showApodErrorObservable.subscribe { view?.displayErrorDialog() }
        ))
    }

    fun updateApod(): Unit = updateApodListSubject.onNext(Unit)

}