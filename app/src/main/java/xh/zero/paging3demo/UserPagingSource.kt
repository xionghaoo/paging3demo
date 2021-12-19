package xh.zero.paging3demo

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay
import java.util.*

class UserPagingSource(
    val query: String
) : PagingSource<Int, User>() {
    companion object {
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
//            val response = backend.searchUsers(query, nextPageNumber)
            delay(1000)
            val num = nextPageNumber - 1
            var response: List<User> = listOf(
                User(0 + num * 5, "name_$nextPageNumber"),
                User(1 + num * 5, "name_$nextPageNumber"),
                User(2 + num * 5, "name_$nextPageNumber"),
                User(3 + num * 5,"name_$nextPageNumber"),
                User(4 + num * 5, "name_$nextPageNumber"),
            )

            if (nextPageNumber >= 5) {
                Log.d(MainActivity.TAG, "Load complete")
                return LoadResult.Page(
                    data = listOf(
                        User(30, "name_$nextPageNumber"),
                    ),
                    prevKey = null, // Only paging forward.
                    nextKey = null
                )
            } else {
                Log.d(MainActivity.TAG, "Load...  -  ${response.size}, page = ${nextPageNumber}")
                return LoadResult.Page(
                    data = response,
                    prevKey = null, // Only paging forward.
                    nextKey = nextPageNumber + 1
                )
            }
        } catch (e: Exception) {
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            return LoadResult.Error<Int, User>(e)
        }
    }
}