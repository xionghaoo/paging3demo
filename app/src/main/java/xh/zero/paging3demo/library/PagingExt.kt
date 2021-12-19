package xh.zero.paging3demo.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn

fun <T: Any> ViewModel.createFlow(
    source: PagingSource<Int, T>,
    pageSize: Int
) = Pager(
    PagingConfig(
        pageSize = pageSize,
        enablePlaceholders = true,
        prefetchDistance = 1
    )
) {
    source
}.flow