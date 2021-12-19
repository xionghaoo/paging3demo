package xh.zero.paging3demo

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserAdapter(lifecycle: LifecycleOwner) : PagingDataAdapter<User, RecyclerView.ViewHolder>(
    diffCallback = User.UserComparator
) {

    init {
        lifecycle.lifecycleScope.launch {
            loadStateFlow.collectLatest {
                loadState = it.append
            }
        }
    }

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

    class UserViewHolder(v: View) : RecyclerView.ViewHolder(v)
    class TailViewHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_CONTENT) {
            holder.itemView.findViewById<TextView>(R.id.tv_name).text = getItem(position)?.id.toString()
        } else {
            holder.itemView.findViewById<TextView>(R.id.tv_name).setBackgroundColor(Color.WHITE)
            holder.itemView.findViewById<TextView>(R.id.tv_name).setTextColor(Color.BLACK)
            holder.itemView.findViewById<TextView>(R.id.tv_name).text = "没有更多数据啦"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_CONTENT) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            return UserViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            return TailViewHolder(v)
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

    companion object {
        private const val VIEW_TYPE_CONTENT = 0
        private const val VIEW_TYPE_TAIL = 1
    }

}