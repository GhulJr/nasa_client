package com.ghuljr.nasaclient.ui.search.search_result

import com.ghuljr.nasaclient.data.model.NasaMediaModel
import com.ghuljr.nasaclient.data.repository.NasaRepository
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class SearchResultPresenter(
    private val nasaRepository: NasaRepository
): BasePresenter<SearchResultView>() {

    private val searchNasaMediaSubject: PublishSubject<String> = PublishSubject.create()

    private val searchNasaMediaObservable: Observable<Resource<List<NasaMediaModel>>> = searchNasaMediaSubject
        .flatMap { nasaRepository.searchNasaMedia(it).toObservable() }
        .startWith(Resource.Loading())
        .share()

    private val searchResultSuccessObservable: Observable<List<NasaMediaModel>> = searchNasaMediaObservable
        .filter { it is Resource.Success }
        .map { it.data ?: listOf() }
        .share()

    private val displaySearchResultObservable: Observable<List<NasaMediaModel>> = searchResultSuccessObservable
        .filter { it.isNotEmpty() }
        .observeOn(AndroidSchedulers.mainThread())

    private val displayNoResultObservable: Observable<Boolean> = searchResultSuccessObservable
        .map { it.isEmpty() }
        .observeOn(AndroidSchedulers.mainThread())

    override fun onViewAttached() {
        super.onViewAttached()

        disposable.set(CompositeDisposable(
            displaySearchResultObservable.subscribe { view?.displaySearchResult(it) },
            displayNoResultObservable.subscribe { view?.displayNoResultsView(it) }
        ))
    }

    fun searchNasaMedia(query: String) = searchNasaMediaSubject.onNext(query)
}