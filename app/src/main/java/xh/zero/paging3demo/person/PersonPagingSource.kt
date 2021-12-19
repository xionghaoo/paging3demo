package xh.zero.paging3demo.person

import android.util.Log
import kotlinx.coroutines.delay
import xh.zero.paging3demo.MainActivity.Companion.TAG

class PersonPagingSource : RemotePagingSource<Person>() {
    override suspend fun onLoad(page: Int): List<Person> {
        delay(500)
        Log.d(TAG, "onLoad: page = $page")
        if (page == 3) throw IllegalArgumentException("加载错误")
        if (page < 5) {
            return List(10) { index ->
                val p = Person()
                p.id = index + (page - 1) * 10
                p.age = index + 20
                p.name = "name$index"
                p
            }
        } else {
            return listOf()
        }
    }

    override suspend fun onError(e: Exception) {

    }
}