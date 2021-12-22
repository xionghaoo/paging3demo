package xh.zero.paging3demo.library

import android.util.Log
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

/**
 * 分页适配器
 *
 * 3.1.0版本retry方法调用无效，用refresh方法替代retry，refresh会尝试从前一页开始加载
 */
abstract class PagingLoadAdapter<T: Any>(
    diffCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    /**
     * 内容项视图
     */
    abstract val itemLayoutId: Int

    /**
     * 加载状态视图
     */
    abstract val tailLayoutId: Int

    private val footerStateAdapter: FooterStateAdapter by lazy {
        FooterStateAdapter(
            tailLayoutId = tailLayoutId,
            onBindTailView = ::onBindTailView
        )
    }

//    init {
//        /**
//         * 数据加载状态监听
//         * LoadState.endOfPaginationReached在LoadStateAdapter中只会等于false，所以用loadStateFlow来监听等于true的情况
//         */
//        lifecycle.lifecycleScope.launch {
//            loadStateFlow.collectLatest {
//                loadState = it.append
//            }
//        }
//    }

    /**
     * 带加载状态的组合适配器
     */
    fun withLoadStateAdapter(): ConcatAdapter = withLoadStateFooter(footerStateAdapter)

    /**
     * 数据加载的状态，用来判定是否显示<没有更多数据>的尾部状态行
     */
//    private var loadState: LoadState = LoadState.NotLoading(false)
//        set(value) {
//            val hadExtraRow = hasExtraRow()
//            field = value
//            val hasExtraRow = hasExtraRow()
//            if (hasExtraRow && !hadExtraRow) {
//                notifyItemInserted(super.getItemCount())
//            } else if (!hasExtraRow && hadExtraRow) {
//                notifyItemRemoved(super.getItemCount())
//            }
//        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindItemView(holder.itemView, getItem(position), position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(itemLayoutId, parent, false))
    }

//    override fun getItemViewType(position: Int): Int {
//        return if (hasExtraRow() && position == itemCount - 1) {
//            VIEW_TYPE_TAIL
//        } else {
//            VIEW_TYPE_CONTENT
//        }
//    }

//    override fun getItemCount(): Int {
//        return super.getItemCount() + if (hasExtraRow()) 1 else 0
//    }

    /**
     * 是否给列表尾部添加状态行
     * 只有在全部页面加载完成时显示<没有更多数据>行，其他状态不添加尾部状态行
     * 像<加载中>和<加载错误>这两种状态用FooterStateAdapter来显示
     */
//    private fun hasExtraRow() : Boolean = loadState is LoadState.NotLoading && loadState.endOfPaginationReached

    abstract fun onBindItemView(v: View, item: T?, position: Int)
    abstract fun onBindTailView(v: View, state: LoadMoreState)

    class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v)

//    companion object {
//        private const val VIEW_TYPE_CONTENT = 0
//        private const val VIEW_TYPE_TAIL = 1
//    }
}

class TailViewHolder(v: View) : RecyclerView.ViewHolder(v)

/**
 * 加载中状态适配器，和主适配器组合，显示<加载中...>和<加载错误>的状态
 * LoadState.endOfPaginationReached在这个Adapter中只会等于false，所以用loadStateFlow来监听等于true的情况
 */
private class FooterStateAdapter(
    private val tailLayoutId: Int,
    private val onBindTailView: (v: View, state: LoadMoreState) -> Unit,
) : LoadStateAdapter<TailViewHolder>() {

    override fun onBindViewHolder(holder: TailViewHolder, loadState: LoadState) {
        val itemView = holder.itemView
        when(loadState) {
            is LoadState.Loading -> onBindTailView(itemView, LoadMoreState.LOADING)
            is LoadState.Error -> onBindTailView(itemView, LoadMoreState.ERROR)
            is LoadState.NotLoading -> {
                if (loadState.endOfPaginationReached) onBindTailView(itemView, LoadMoreState.LOADED)
            }
            else -> {}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): TailViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(tailLayoutId, parent, false)
        return TailViewHolder(v)
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return !(loadState is LoadState.NotLoading && !loadState.endOfPaginationReached)
    }

}

enum class LoadMoreState {
    LOADED, LOADING, ERROR
}