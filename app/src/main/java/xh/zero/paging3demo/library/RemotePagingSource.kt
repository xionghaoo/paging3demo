package xh.zero.paging3demo.library

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import xh.zero.paging3demo.MainActivity

/**
 * 分页加载数据源
 */
abstract class RemotePagingSource<R: Any> : PagingSource<Int, R>() {
    override fun getRefreshKey(state: PagingState<Int, R>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, R> {
        return try {
            val nextPage = params.key ?: 1
            val result = onLoad(nextPage)
            Log.d(MainActivity.TAG, "load: ${result.isEmpty()}, ${nextPage + 1}")

            LoadResult.Page(
                data = result,
                prevKey = if (nextPage <= 1) null else nextPage - 1,
                nextKey = if (result.isEmpty()) null else nextPage + 1
            )
        } catch (e: Exception) {
            onError(e)
            LoadResult.Error(e)
        }
    }

    abstract suspend fun onLoad(page: Int) : List<R>

    abstract suspend fun onError(e: Exception)
}