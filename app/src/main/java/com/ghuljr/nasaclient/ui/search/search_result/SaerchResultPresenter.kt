package com.ghuljr.nasaclient.ui.search.search_result

import android.util.Log
import com.ghuljr.nasaclient.data.model.NasaMediaModel
import com.ghuljr.nasaclient.data.repository.NasaRepository
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class SearchResultPresenter(
    private val nasaRepository: NasaRepository
) : BasePresenter<SearchResultView>() {

    private val searchNasaMediaSubject: BehaviorSubject<String> = BehaviorSubject.create()

    private val searchNasaMediaObservable: Observable<Resource<List<NasaMediaModel>>> =
        searchNasaMediaSubject
            .flatMap { nasaRepository.searchNasaMedia(it).toObservable() }
            .startWith(Resource.Loading())
            .replay(1)
            .refCount()

    private val searchResultSuccessObservable: Observable<List<NasaMediaModel>> = searchNasaMediaObservable
        .filter { it is Resource.Success }
        .map { it.data ?: listOf() }
        .replay(1)
        .refCount()

    private val setSearchResultObservable: Observable<List<NasaMediaModel>> = searchResultSuccessObservable
        .filter { it.isNotEmpty() }
        .observeOn(AndroidSchedulers.mainThread())

    private val isResultEmptyObservable: Observable<Boolean> = searchResultSuccessObservable
        .map { it.isEmpty() }
        .observeOn(AndroidSchedulers.mainThread())

    private val isDataLoadingObservable: Observable<Boolean> = searchNasaMediaObservable
        .map { it is Resource.Loading }
        .observeOn(AndroidSchedulers.mainThread())

    override fun onViewAttached() {
        super.onViewAttached()

        disposable.set(CompositeDisposable(
            setSearchResultObservable.subscribe { view?.displaySearchResult(it) },
            isResultEmptyObservable.subscribe { view?.displayNoResultsView(it) },
            isDataLoadingObservable.subscribe { view?.displayLoadingViews(it) }
        ))
    }

    fun searchNasaMedia(query: String) = searchNasaMediaSubject.onNext(query)
}