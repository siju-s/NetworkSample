package com.siju.networksample.di

import com.siju.networksample.data.remote.NetworkModule
import com.siju.networksample.data.repository.ItemRepository
import com.siju.networksample.data.repository.ItemRepositoryImpl
import com.siju.networksample.domain.usecase.GetItemsUseCase

object AppModule {

    private val repository: ItemRepository by lazy {
        ItemRepositoryImpl(NetworkModule.apiService)
    }

    val getItemsUseCase: GetItemsUseCase by lazy {
        GetItemsUseCase(repository)
    }
}