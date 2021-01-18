package com.example.inmobilestask.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inmobilestask.R
import com.example.inmobilestask.data.models.Items
import com.example.inmobilestask.data.models.NetworkState
import com.example.inmobilestask.data.network.MyApi
import com.example.inmobilestask.data.network.NetworkConnectionInterceptor
import com.example.inmobilestask.data.repositories.Repository
import com.example.inmobilestask.ui.viewmodels.MainActivityViewModel
import com.example.inmobilestask.ui.viewmodels.ViewModelFactory
import com.example.inmobilestask.utils.hide
import com.example.inmobilestask.utils.show
import com.example.inmobilestask.utils.snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var isItemsAsc: Boolean = false
    val layoutManager = LinearLayoutManager(this)
    private var isLoading: Boolean = false
    private lateinit var factory: ViewModelFactory
    private lateinit var adapter: RepoListAdapter
    private lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createDependencies()
        setUpUi()
        setUpViewModel()
    }

    private fun setUpViewModel() {
        // With ViewModelFactory
        viewModel = ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)
        viewModel.getRepositoriesData()

        viewModel.networkResponse.observe(this, Observer { response ->
            if (response != null) {
                when (response) {
                    is NetworkState.Loading -> onLoadingUI()
                    is NetworkState.Success -> onSuccessUpdateUI(getItems(response))
                    is NetworkState.Error -> onFailureUpdateUI(response.error)
                    else -> print("otherwise")
                }
            }
        })

    }


    private fun createDependencies() { // creating depenencies for viewmodel, repository, api class, network interceptor
        //could've used Koin/Kodein for DI
        val networkConnectionInterceptor = NetworkConnectionInterceptor(this)
        val myApi = MyApi(networkConnectionInterceptor)
        val repository = Repository(myApi)
        factory = ViewModelFactory(repository)
    }

    private fun getItems(response: NetworkState.Success): List<Items> =
        if (isItemsAsc)  // UI-related function to indentify when user chooses asc/desc option
            response.data.itemsAsc
        else
            response.data.items


    override fun onResume() {
        super.onResume()
        timerView.start()
        timerView.currentTime = viewModel.currentTime
    }

    override fun onPause() {
        super.onPause()
        timerView.stop()
        viewModel.currentTime = timerView.currentTime  // preserving time value on screen rotation
    }

    override fun onDestroy() {
        super.onDestroy()
        timerView.reset()
    }


    private fun setUpUi() {
        adapter = RepoListAdapter(arrayListOf()) {
            startItemDetailFragment(it, true)
        }

        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter

        order_btn?.setOnCheckedChangeListener { switchView, isChecked ->
            isItemsAsc = isChecked
            viewModel.getItemsListAscOrder(isChecked)
        }



        recyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() { // recyclerview scroll listener
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == items.size - 1) {
                        adapter.addLoadingItem()
                        viewModel.getMorePagesData()
                        isLoading = true
                    }
                }
            }
        })

        refresh_btn.setOnClickListener {
            viewModel.getRepositoriesData()
            refresh_btn.visibility = View.GONE
        }
    }


    private lateinit var items: List<Items>

    private fun onSuccessUpdateUI(items: List<Items>) {
        this.items = items
        isLoading = false
        progressBar.hide()
        adapter.setList(this.items)
    }

    private fun onFailureUpdateUI(message: String) {
        isLoading = false
        progressBar.hide()
        parent_layout.snackbar(message)
        refresh_btn.visibility = View.VISIBLE
    }

    private fun onLoadingUI() {
        progressBar.show()
    }


    private fun startItemDetailFragment(
        item: Items,
        addToBackStack: Boolean = false
    ) { // add fragment
        var detailFragment: DetailFragment = DetailFragment.newInstance(item)
        val transaction = supportFragmentManager.beginTransaction()
        if (addToBackStack)
            transaction.addToBackStack(null)
        transaction.add(R.id.container, detailFragment).commit()
    }

}
