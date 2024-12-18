package com.siju.networksample.data.remote

import com.siju.networksample.data.model.ItemDto
import retrofit2.http.GET

interface ApiService {
    @GET
    fun getItems():List<ItemDto>
}