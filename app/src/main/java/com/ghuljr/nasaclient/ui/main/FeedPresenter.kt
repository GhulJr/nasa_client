package com.ghuljr.nasaclient.ui.main

import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.repository.NasaRepository
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter
import com.ghuljr.nasaclient.ui.common.NetworkError
import com.ghuljr.nasaclient.ui.common.ResourceError
import com.ghuljr.nasaclient.utils.DAY_TIMESTAMP
import com.ghuljr.nasaclient.utils.isDateExpired
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class FeedPresenter(
    private val nasaRepository: NasaRepository
) : BasePresenter<FeedView>() {

    private val refreshApodSubject: BehaviorSubject<Unit> = BehaviorSubject.create()
    private val openCurrentApodDetailsSubject: PublishSubject<Unit> = PublishSubject.create()
    private val openApodDetailsSubject: PublishSubject<ApodModel> = PublishSubject.create()


    private val displayApodObservable: Observable<ApodModel> = nasaRepository.getLatestApod()
        .subscribeOn(Schedulers.io())
        .replay(1).refCount()
        .observeOn(AndroidSchedulers.mainThread())

    private val isDataUpdatedObservable: Observable<Boolean> = displayApodObservable
        .map { !it.date.isDateExpired(DAY_TIMESTAMP) }

    private val apodArchiveObservable: Observable<List<ApodModel>> = nasaRepository.getApodList()
        .subscribeOn(Schedulers.io())
        .map { if (it.isNotEmpty()) it.drop(1) else it }
        .share()


    private val isApodArchiveVisibleObservable: Observable<Boolean> = apodArchiveObservable
        .map { it.isNotEmpty() }
        .observeOn(AndroidSchedulers.mainThread())

    private val setApodArchiveObservable: Observable<List<ApodModel>> = apodArchiveObservable
        .filter { it.isNotEmpty() }
        .observeOn(AndroidSchedulers.mainThread())

    private val refreshApodObservable: Observable<Resource<ApodModel>> = refreshApodSubject
        .subscribeOn(Schedulers.io())
        .switchMap { nasaRepository.updateApod().startWith(Resource.Loading()) }
        .share()

    private val refreshApodLoadingObservable: Observable<Boolean> = refreshApodObservable
        .map { it is Resource.Loading }
        .observeOn(AndroidSchedulers.mainThread())

    private val refreshApodErrorObservable: Observable<ResourceError> = refreshApodObservable
        .filter { it is Resource.Error }
        .map { it.error ?: NetworkError.ResponseError }
        .observeOn(AndroidSchedulers.mainThread())

    private val openApodDetailsObservable: Observable<ApodModel> = Observable.merge(
        openCurrentApodDetailsSubject.switchMap { displayApodObservable },
        openApodDetailsSubject
    )


    override fun onViewAttached() {
        super.onViewAttached()

        disposable.set(
            CompositeDisposable(
                displayApodObservable.subscribe { view?.diplayApod(it) },
                refreshApodErrorObservable.subscribe { view?.displayApodError(it) },
                isApodArchiveVisibleObservable.subscribe { view?.setApodArchiveVisibility(it) },
                setApodArchiveObservable.subscribe { view?.setApodArchiveList(it) },
                refreshApodLoadingObservable.subscribe { /*TODO: make skeleton loader*/ },
                openApodDetailsObservable.subscribe { view?.openApodDetails(it) },
                isDataUpdatedObservable.subscribe { if (!it) refreshApod() }
            )
        )
    }

    fun refreshApod(): Unit = refreshApodSubject.onNext(Unit)
    fun openCurrentApodDetails(): Unit = openCurrentApodDetailsSubject.onNext(Unit)
    fun openApodDetails(apod: ApodModel): Unit = openApodDetailsSubject.onNext(apod)

    companion object {
        private const val TAG = "FeedPresenter"
    }
}

