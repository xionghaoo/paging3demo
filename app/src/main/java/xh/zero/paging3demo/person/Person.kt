package xh.zero.paging3demo.person

import androidx.recyclerview.widget.DiffUtil

class Person {
    var id: Int = 0
    var name: String? = null
    var age: Int = 0
    var money: Int = 0

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Person) {
            return id == other.id
        } else {
            return false
        }
    }

    object DIFF : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem == newItem
        }
    }
}