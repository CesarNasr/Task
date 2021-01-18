package com.example.inmobilestask.data.repositories

import com.example.inmobilestask.data.models.Items
import com.example.inmobilestask.data.models.ServerResponse
import com.example.inmobilestask.data.network.MyApi
import com.example.inmobilestask.data.network.SafeApiRequest
import com.example.inmobilestask.utils.*

class Repository(private val api: MyApi) : SafeApiRequest() {
// repository communicates with the network (could be any data source) and returns result to the viewModel
    suspend fun getApiRepositories(pageNumber: Int = 0): ServerResponse {
        return apiRequest {
            api.getRepositoriesApiCall(apiDateValue, apiSortStar, apiDecOrder, pageNumber)
        }
    }

    fun getItemsAscOrder(response: ServerResponse?): MutableList<Items> {
        return Utilities().ascOrderSorting(response?.items!!)
    }
}