package com.ghuljr.nasaclient.ui.splash

import com.ghuljr.nasaclient.data.repository.NasaRepository
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class SplashPresenter(
    private val nasaRepository: NasaRepository
) : BasePresenter<SplashView>() {

    private val updateApodListObservable: PublishSubject<Unit> = PublishSubject.create()
    private val showErrorSubject: PublishSubject<Unit> = PublishSubject.create()

    private val redirectToAppObservable: Observable<Resource<Void>> = updateApodListObservable
        .flatMap { nasaRepository.updateApodList() }

    override fun onViewAttached() {
        super.onViewAttached()
        disposable.set(CompositeDisposable(
            redirectToAppObservable.subscribe {

            }
        ))
    }

}