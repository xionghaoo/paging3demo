package xh.zero.paging3demo.person

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import xh.zero.paging3demo.MainActivity.Companion.TAG
import xh.zero.paging3demo.R
import xh.zero.paging3demo.library.LoadMoreState
import xh.zero.paging3demo.library.PagingLoadAdapter

class PersonAdapter(
    private val lifecycleOwner: LifecycleOwner
) : PagingLoadAdapter<Person>(lifecycleOwner, Person.DIFF) {

    override val itemLayoutId: Int
        get() = R.layout.list_item

    override val tailLayoutId: Int
        get() = R.layout.list_load_more

    override fun onBindItemView(v: View, item: Person?, position: Int) {
        v.findViewById<TextView>(R.id.tv_name).text = getItem(position)?.id.toString()
    }

    override fun onBindTailView(v: View, state: LoadMoreState) {
        val tv = v.findViewById<TextView>(R.id.tv_load_more)
        Log.d(TAG, "onBindTailView: load state =  $state")
        when (state) {
            LoadMoreState.LOADED -> {
                tv.text = "没有更多数据啦"
            }
            LoadMoreState.LOADING -> {
                tv.text = "加载中..."
            }
            LoadMoreState.ERROR -> {
                tv.text = "加载失败，请重试"
                tv.setOnClickListener {
                    // retry调用无效，refresh会尝试重前一页开始加载页面
                    refresh()
                }
            }
        }
    }
}