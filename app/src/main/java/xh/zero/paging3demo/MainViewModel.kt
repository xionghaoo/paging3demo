package xh.zero.paging3demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import xh.zero.paging3demo.person.PersonPagingSource
import xh.zero.paging3demo.library.createFlow
import xh.zero.paging3demo.person.Person

class MainViewModel : ViewModel() {

    val flow = Pager(
        PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            prefetchDistance = 2,
            initialLoadSize = 2
        )
    ) {
        PersonPagingSource()
    }.flow

}