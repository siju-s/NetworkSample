package com.siju.networksample.data.repository

import com.siju.networksample.domain.model.Item

interface ItemRepository {
    suspend fun fetchItems(): List<Item>
}