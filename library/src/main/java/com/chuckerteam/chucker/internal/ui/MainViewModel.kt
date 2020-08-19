package com.chuckerteam.chucker.internal.ui

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.chuckerteam.chucker.internal.data.entity.RecordedThrowableTuple
import com.chuckerteam.chucker.internal.data.entity.ThrowableType
import com.chuckerteam.chucker.internal.data.repository.RepositoryProvider
import com.chuckerteam.chucker.internal.support.NotificationHelper
import kotlinx.coroutines.launch

internal class MainViewModel : ViewModel() {

    private val currentFilter = MutableLiveData<String>("")

    fun getLiveData(tag: String):LiveData<List<RecordedThrowableTuple>>{
        return RepositoryProvider.throwable()
                .getSortedThrowablesTuples(tag)
    }

    fun getSearchResults(text: String):LiveData<List<RecordedThrowableTuple>>{
        return RepositoryProvider.throwable()
                .getSearchResults(text)
    }

    fun updateItemsFilter(searchQuery: String) {
        currentFilter.value = searchQuery
    }

    fun clearThrowables(tag:String) {
        viewModelScope.launch {
            RepositoryProvider.throwable().deleteAllThrowables(tag)
        }
    }
}
