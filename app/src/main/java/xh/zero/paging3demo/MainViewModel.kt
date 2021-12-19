package xh.zero.paging3demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import xh.zero.paging3demo.person.PersonPagingSource
import xh.zero.paging3demo.person.createFlow

class MainViewModel : ViewModel() {
    val flow = Pager(
        PagingConfig(pageSize = 5, enablePlaceholders = true, prefetchDistance = 1)
    ) {
        UserPagingSource()
    }.flow.cachedIn(viewModelScope)


    fun flow() = createFlow(
        source = PersonPagingSource(),
        pageSize = 5
    )

}