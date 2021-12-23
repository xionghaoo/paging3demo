package xh.zero.paging3demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import xh.zero.paging3demo.library.dataSourceFlow
import xh.zero.paging3demo.person.PersonPagingSource

class MainViewModel : ViewModel() {

    val flow = dataSourceFlow(10) { PersonPagingSource() }

}