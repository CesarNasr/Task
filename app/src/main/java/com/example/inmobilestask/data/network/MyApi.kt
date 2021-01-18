package com.example.inmobilestask.data.network

import androidx.lifecycle.LiveData
import com.example.inmobilestask.data.models.ServerResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface MyApi {


//    @FormUrlEncoded
    @GET("search/repositories") //?
    suspend fun getRepositoriesApiCall(
        @Query("q") dateCreated: String?,
        @Query("sort") sort: String?,
        @Query("order") order: String?,
        @Query("page") page: Int?
    ): Response<ServerResponse>

//    @GET("quotes")
//    suspend fun getQuotes(): Response<QuotesResponse>


    companion object {
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor): MyApi {

            val okHttpClient =
                OkHttpClient.Builder().addInterceptor(networkConnectionInterceptor).build()

            return Retrofit.Builder().client(okHttpClient)
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(MyApi::class.java)
        }
    }
}