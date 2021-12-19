package xh.zero.paging3demo.person

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class PagingLoadAdapter<T: Any>(
    lifecycle: LifecycleOwner,
    diffCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    abstract val itemLayoutId: Int
    abstract val tailLayoutId: Int

    private var footerStateAdapter: FooterStateAdapter? = null

    init {
        /**
         * 数据加载状态监听
         */
        lifecycle.lifecycleScope.launch {
            loadStateFlow.collectLatest {
                loadState = it.append
            }
        }
    }

    /**
     * 带加载状态的组合适配器
     */
    fun withLoadStateAdapter(): ConcatAdapter {
        footerStateAdapter = FooterStateAdapter(
            tailLayoutId = tailLayoutId,
            onBindTailView = ::onBindTailView
        )
        return withLoadStateFooter(footerStateAdapter!!)
    }

    /**
     * 数据加载的状态，用来判定是否显示<没有更多数据>的UI
     */
    private var loadState: LoadState = LoadState.NotLoading(false)
        set(value) {
            val hadExtraRow = hasExtraRow()
            field = value
            val hasExtraRow = hasExtraRow()
            if (hasExtraRow && !hadExtraRow) {
                notifyItemInserted(super.getItemCount())
            } else if (!hasExtraRow && hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_CONTENT) {
            onBindItemView(holder.itemView, getItem(position), position)
        } else {
            onBindTailView(holder.itemView, LoadMoreState.LOADED)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_CONTENT) {
            ItemViewHolder(inflater.inflate(itemLayoutId, parent, false))
        } else {
            TailViewHolder(inflater.inflate(tailLayoutId, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            VIEW_TYPE_TAIL
        } else {
            VIEW_TYPE_CONTENT
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow() : Boolean = loadState is LoadState.NotLoading && loadState.endOfPaginationReached

    abstract fun onBindItemView(v: View, item: T?, position: Int)
    abstract fun onBindTailView(v: View, state: LoadMoreState)

    class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v)

    companion object {
        private const val VIEW_TYPE_CONTENT = 0
        private const val VIEW_TYPE_TAIL = 1
    }
}

class TailViewHolder(v: View) : RecyclerView.ViewHolder(v)

/**
 * 加载中状态适配器，和主适配器组合，显示<加载中...>的状态
 */
private class FooterStateAdapter(
    private val tailLayoutId: Int,
    private val onBindTailView: (v: View, state: LoadMoreState) -> Unit,
) : LoadStateAdapter<TailViewHolder>() {

    override fun onBindViewHolder(holder: TailViewHolder, loadState: LoadState) {
        val itemView = holder.itemView
        when(loadState) {
            is LoadState.Loading -> {
                onBindTailView(itemView, LoadMoreState.LOADING)
            }
            is LoadState.Error -> {
                onBindTailView(itemView, LoadMoreState.ERROR)
            }
            else -> {}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): TailViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(tailLayoutId, parent, false)
        return TailViewHolder(v)
    }
}

enum class LoadMoreState {
    LOADED, LOADING, ERROR
}