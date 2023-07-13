package com.chugunov.testappcompose.domain

import com.chugunov.testappcompose.domain.entity.User

/**
 * Интерфейс Repository с функцией getUserList
 */
interface Repository {

    suspend fun getUsersList(): List<User>
}