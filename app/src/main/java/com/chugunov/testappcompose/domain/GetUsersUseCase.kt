package com.chugunov.testappcompose.domain

import com.chugunov.testappcompose.domain.entity.User

/**
 * GetUsersUseCase, который явялется интерактором, ответсвенный за выполнение операции получения
 * списка пользователей
 */
class GetUsersUseCase(private val repository: Repository) {

    suspend fun getUsers(): List<User> {
        return repository.getUsersList()
    }
}