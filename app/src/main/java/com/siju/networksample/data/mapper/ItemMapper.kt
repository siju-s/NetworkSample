package com.siju.networksample.data.mapper

import com.siju.networksample.data.model.ItemDto
import com.siju.networksample.domain.model.Item

object ItemMapper {
    fun mapToDomain(dto: ItemDto): Item {
        return Item(
            id = dto.id,
            listId = dto.listId,
            name = dto.name
        )
    }
}