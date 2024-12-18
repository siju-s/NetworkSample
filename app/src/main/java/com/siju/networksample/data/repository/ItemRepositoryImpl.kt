package com.siju.networksample.data.repository

import com.siju.networksample.data.mapper.ItemMapper
import com.siju.networksample.data.remote.ApiService
import com.siju.networksample.domain.model.Item

class ItemRepositoryImpl(
    private val apiService: ApiService
) : ItemRepository {
    override suspend fun fetchItems(): List<Item> {
        val items = apiService.getItems()
        return items.map { ItemMapper.mapToDomain(it) }
    }
}