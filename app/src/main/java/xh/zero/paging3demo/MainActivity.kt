package xh.zero.paging3demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.insertFooterItem
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xh.zero.paging3demo.person.PersonAdapter

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private val adapter: PersonAdapter by lazy { PersonAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.button)
        val rcList = findViewById<RecyclerView>(R.id.rc_list)
        rcList.layoutManager = LinearLayoutManager(this)
        // 注意这里需要赋值ConcatAdapter
        rcList.adapter = adapter.withLoadStateAdapter()

        btn.setOnClickListener {
            /**
             * Paging3的数据刷新Bug，页面回到初始位置时刷新，第二页的加载不会被触发
             * 这里需要重置下Adapter
             */
            rcList.swapAdapter(adapter.withLoadStateAdapter(), true)
            /**
             * 刷新列表，这里调adapter.refresh()或者loadData()都可以
             */
            adapter.refresh()
        }

        loadData()

        findViewById<Button>(R.id.btn_retry).setOnClickListener {
            // PagingSource返回Error时才有效
            adapter.retry()
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.flow.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    companion object {
        const val TAG = "PagingLog"
    }
}