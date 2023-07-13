package com.chugunov.testappcompose.data

import com.chugunov.testappcompose.data.mappers.UsersMapper
import com.chugunov.testappcompose.data.network.ApiService
import com.chugunov.testappcompose.domain.Repository
import com.chugunov.testappcompose.domain.entity.User


/**
 * Имплементация репозитория. В параметрах передается ApiService - имитация сервиса и
 * usersMapper для преобразования сущностей
 */
class RepositoryImpl(
    private val apiService: ApiService,
    private val usersMapper: UsersMapper
) : Repository {


    //Переопределенный метод getUsersList с интерфейса репозитория
    override suspend fun getUsersList(): List<User> {
        val usersDto = apiService.getUsersListDto()
        return usersMapper.mapToDomain(usersDto)
    }
}