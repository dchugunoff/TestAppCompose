package com.chugunov.testappcompose.data.mappers

import com.chugunov.testappcompose.data.network.UserModelDto
import com.chugunov.testappcompose.data.network.UsersDto
import com.chugunov.testappcompose.domain.entity.User

class UsersMapper {

    /**
     * Класс маппера, который хранит в себе 2 функции для преобразования сущностей UserDto в User
     */

    fun mapToDomain(usersDto: UsersDto): List<User> {
        return usersDto.users.map { mapToDomain(it) }
    }

    private fun mapToDomain(userDto: UserModelDto): User {
        return User(userDto.name, userDto.age)
    }
}