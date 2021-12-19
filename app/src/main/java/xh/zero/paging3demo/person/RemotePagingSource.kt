package xh.zero.paging3demo.person

import androidx.paging.PagingSource
import androidx.paging.PagingState

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
            LoadResult.Page(
                data = result,
                prevKey = null,
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