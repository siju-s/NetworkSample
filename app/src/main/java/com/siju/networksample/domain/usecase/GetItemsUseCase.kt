package com.siju.networksample.domain.usecase

import com.siju.networksample.data.repository.ItemRepository
import com.siju.networksample.domain.model.Item

private const val DELIMITER = "Item "
class GetItemsUseCase(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(): List<Item> {
        val items = repository.fetchItems()
        // Filter out items with blank or null names
        val filtered = items.filter { !it.name.isNullOrBlank() }
        // Sort by listId and then by name
        return filtered.sortedWith(compareBy<Item> { it.listId }.thenBy {
            // Extract the digits from the name, assuming the format is "Item <number>"
            val numericPart = it.name?.substringAfter(DELIMITER)?.toIntOrNull() ?: Int.MAX_VALUE
            numericPart
        })
    }
}