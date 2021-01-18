package com.example.inmobilestask.data.models

data class ServerResponse (

    val total_count : Int,
    val incomplete_results : Boolean,
    var items : MutableList<Items>,
    var itemsAsc : MutableList<Items>

)