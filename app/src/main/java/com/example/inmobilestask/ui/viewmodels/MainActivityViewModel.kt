package com.example.inmobilestask.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inmobilestask.data.models.Items
import com.example.inmobilestask.data.models.NetworkState
import com.example.inmobilestask.data.models.ServerResponse
import com.example.inmobilestask.data.repositories.Repository
import com.example.inmobilestask.utils.ApiException
import com.example.inmobilestask.utils.Coroutines
import com.example.inmobilestask.utils.NoInternetException
import com.example.inmobilestask.utils.apiDecOrder

class MainActivityViewModel(private val repository: Repository) : ViewModel() {
    var networkResponse = MutableLiveData<NetworkState>()
    private var pageNumber = 0
    private var response: ServerResponse? = null
    private var initialItems: List<Items>? = null

    var currentTime : Long= 0

    fun getItemsListAscOrder(ascOrder: Boolean) { // sorts the list appearing on screen and returns it to UI
        if (!response?.items?.isNullOrEmpty()!!) {
            if (ascOrder) {
                response?.itemsAsc = repository.getItemsAscOrder(response)
            }
            networkResponse.value = NetworkState.Success(response!!)
        }
    }


    fun getMorePagesData() { // function that talks to the repository in order to get data from more pages from the network API
        pageNumber++
        Coroutines.main {
            try {
                val newItems = repository.getApiRepositories(pageNumber).items
                response?.items?.addAll(newItems)
                response?.let {
                    networkResponse.value = NetworkState.Success(response!!)

                }
                return@main

            } catch (e: ApiException) {
                networkResponse.value = NetworkState.Error(e.message!!)

            } catch (e: NoInternetException) {
                networkResponse.value = NetworkState.Error(e.message!!)

            }
        }
    }


    fun getRepositoriesData() {
        //1- change the state of network in the UI to Loading through LiveData Observer
        //2- call repository which calls network
        //3- receive result and update network state in the UI and update data as well

        response?.let {// if data already exists return it (might happen on screen rotation
            networkResponse.value = NetworkState.Success(response!!)
            return
        }


        networkResponse.value = NetworkState.Loading

        Coroutines.main {
            try {
                response = repository.getApiRepositories()
                initialItems = response?.items

                response?.let {
                    networkResponse.value = NetworkState.Success(response!!)
                }
                return@main

            } catch (e: ApiException) {
                networkResponse.value = NetworkState.Error(e.message!!)
            } catch (e: NoInternetException) {
                networkResponse.value = NetworkState.Error(e.message!!)
            }
        }
    }
}