package com.siju.networksample.data.model

import com.google.gson.annotations.SerializedName

data class ItemDto(
    @SerializedName("id") val id: Int,
    @SerializedName("listId") val listId: Int,
    @SerializedName("name") val name: String?
)