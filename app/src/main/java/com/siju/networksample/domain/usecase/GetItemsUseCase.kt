package com.siju.networksample.domain.usecase

import com.siju.networksample.data.repository.ItemRepository
import com.siju.networksample.domain.model.Item
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val DELIMITER = "Item "
class GetItemsUseCase(private val repository: ItemRepository,
                      private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default) {

    suspend operator fun invoke(): List<Item> =
        withContext(defaultDispatcher) {
            val items = repository.fetchItems()
            // Filter out items with blank or null names
            val filtered = items.filter { !it.name.isNullOrBlank() }
            // Sort by listId and then by name
            filtered.sortedWith(compareBy<Item> { it.listId }.thenBy {
                // Extract the digits from the name, assuming the format is "Item <number>"
                val numericPart = it.name?.substringAfter(DELIMITER)?.toIntOrNull() ?: Int.MAX_VALUE
                numericPart
            })
            filtered
        }
    }