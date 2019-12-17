//package com.example.tagihin.view.search
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.example.tagihin.data.remote.model.BaseBill
//import com.example.tagihin.data.remote.repository.SearchRepository
//import io.reactivex.disposables.Disposable
//import io.reactivex.schedulers.Schedulers
//
//class SearchViewModel (val repo : SearchRepository) : ViewModel(){
//    private var disposable : Disposable? = null
//    val searchResult : MutableLiveData<List<BaseBill>> = MutableLiveData()
//    val error : MutableLiveData<String> = MutableLiveData()
//    fun search(query : String){
//            disposable = repo.search(query)
//                .subscribeOn(Schedulers.io())
//                .subscribe({
//                    searchResult.postValue(it.body()?.data)
//                }, {
//                    error.postValue(it.message)
//                })
//    }
//}