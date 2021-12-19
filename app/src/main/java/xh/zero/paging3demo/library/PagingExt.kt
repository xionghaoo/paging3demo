package xh.zero.paging3demo.library

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource


//val flow = Pager(
//    PagingConfig(
//        pageSize = 10,
//        enablePlaceholders = false,
//        prefetchDistance = 2,
//        initialLoadSize = 2
//    )
//) {
//    PersonPagingSource()
//}.flow

fun <T: Any> ViewModel.dataSourceFlow(pageSize: Int, pagingSourceFactory: () -> PagingSource<Int, T>) =
    Pager(
        PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false,
            prefetchDistance = 2,
            initialLoadSize = 2
        ),
        pagingSourceFactory = pagingSourceFactory
    ).flow