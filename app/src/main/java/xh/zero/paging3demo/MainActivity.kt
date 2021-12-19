package xh.zero.paging3demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xh.zero.paging3demo.person.PersonAdapter

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "PagingLog"
    }
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var adapter: UserAdapter
    private lateinit var loadMoreStateAdapter: LoadMoreStateAdapter

    private lateinit var personAdapter: PersonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        adapter = UserAdapter(this)

        val btn = findViewById<Button>(R.id.button)
        val rcList = findViewById<RecyclerView>(R.id.rc_list)
        rcList.layoutManager = LinearLayoutManager(this)
        personAdapter = PersonAdapter(this)
        rcList.adapter = personAdapter.withLoadStateAdapter()

        lifecycleScope.launch {
            viewModel.flow().collectLatest {
                personAdapter.submitData(it)
            }
        }

//        loadMoreStateAdapter = LoadMoreStateAdapter {
//            adapter.retry()
//        }
//        rcList.adapter = adapter.withLoadStateFooter(loadMoreStateAdapter)

//        lifecycleScope.launch {
//            // 获取最新的数据
//            viewModel.flow.collectLatest { pagingData ->
//                adapter.submitData(pagingData)
//            }
//        }
//
//        btn.setOnClickListener {
//            adapter.refresh()
//        }
    }
}