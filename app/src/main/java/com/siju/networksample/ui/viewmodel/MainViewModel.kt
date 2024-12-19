package com.siju.networksample.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siju.networksample.domain.usecase.GetItemsUseCase
import com.siju.networksample.ui.model.UiItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val getItemsUseCase: GetItemsUseCase
) : ViewModel() {

    private val _items = MutableStateFlow<Map<Int, List<UiItem>>>(emptyMap())
    val items: StateFlow<Map<Int, List<UiItem>>> = _items.asStateFlow()

    fun loadItems() {
        viewModelScope.launch {
            val domainItems = getItemsUseCase()
            // Group by listId
            val grouped = domainItems.groupBy { it.listId }
            // Map to UI model
            val uiMap = grouped.mapValues { entry ->
                entry.value.map {
                    UiItem(id = it.id, listId = it.listId, name = it.name ?: "")
                }
            }
            _items.value = uiMap
        }
    }
}
