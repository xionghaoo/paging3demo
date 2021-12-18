package xh.zero.paging3demo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView

class LoadMoreStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadMoreStateAdapter.LoadViewHolder>() {
    class LoadViewHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onBindViewHolder(holder: LoadViewHolder, loadState: LoadState) {
        Log.d(MainActivity.TAG, "onBindViewHolder: $loadState")
        (holder.itemView as TextView).text = when(loadState) {
            is LoadState.Loading -> {
                Log.d(MainActivity.TAG, "加载中。。。")
                "加载中..."
            }
            is LoadState.NotLoading -> {
                Log.d(MainActivity.TAG, "没有更多啦")
                if (loadState.endOfPaginationReached) "没有更多啦" else ""
            }
            is LoadState.Error -> {
                Log.d(MainActivity.TAG, "加载错误请重试")
                "加载错误, 请重试"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadViewHolder {
        Log.d(MainActivity.TAG, "onCreateViewHolder: $loadState")

        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_load_more, parent, false)
        return LoadViewHolder(v)
    }
}