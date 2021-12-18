package xh.zero.paging3demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class UserAdapter() : PagingDataAdapter<User, UserAdapter.UserViewHolder>(
    diffCallback = User.UserComparator
) {
    class UserViewHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.tv_name).text = getItem(position)?.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return UserViewHolder(v)
    }
}