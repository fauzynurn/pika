package com.example.tagihin.view.officer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.tagihin.data.remote.repository.OfficerRepository

class OfficerListViewModel(val repo : OfficerRepository) : ViewModel(){
    var query : MutableLiveData<String> = MutableLiveData()

    private val localSearch = Transformations.map(query) {
        repo.getOfficerList(it)
    }
    val searchResult = Transformations.switchMap(localSearch) { it.pagedList }
    val networkState = Transformations.switchMap(localSearch) { it.networkState }
//    val refreshState = Transformations.switchMap(localSearch) { it.refreshState }

    fun retry() {
        val listing = localSearch.value
        listing?.retry?.invoke()
    }
}