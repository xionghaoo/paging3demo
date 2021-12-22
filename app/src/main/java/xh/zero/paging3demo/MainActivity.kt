package xh.zero.paging3demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.insertFooterItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xh.zero.paging3demo.person.PersonAdapter

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "PagingLog"
    }
    private val viewModel by viewModels<MainViewModel>()

    private val adapter: PersonAdapter by lazy { PersonAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.button)
        val rcList = findViewById<RecyclerView>(R.id.rc_list)
        rcList.layoutManager = LinearLayoutManager(this)
        // 注意这里需要赋值ConcatAdapter
        rcList.adapter = adapter.withLoadStateAdapter()

        btn.setOnClickListener {
            // 下拉刷新时重建数据流，Adapter需要重新设置下
            rcList.adapter = adapter.withLoadStateAdapter()
            loadData()
        }

        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.flow.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}