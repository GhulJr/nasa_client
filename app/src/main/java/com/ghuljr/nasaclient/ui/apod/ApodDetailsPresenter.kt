package com.ghuljr.nasaclient.ui.apod

import android.util.Log
import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.repository.NasaRepository
import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

//TODO: include nav args.
class ApodDetailsPresenter(
    private val nasaRepository: NasaRepository
) : BasePresenter<ApodDetailsView>() {

    private val getApodDetailsSubject: BehaviorSubject<Long> = BehaviorSubject.create()

    private val getApodDetailsObservable: Observable<ApodModel> = getApodDetailsSubject
        .flatMap { nasaRepository.getApodById(it) }
        .observeOn(AndroidSchedulers.mainThread())

    override fun onViewAttached() {
        super.onViewAttached()

        disposable.set(CompositeDisposable(
            getApodDetailsObservable.subscribe { view?.displayApod(it) }
        ))
    }

    fun getApodDetails(id: Long) = getApodDetailsSubject.onNext(id)
}