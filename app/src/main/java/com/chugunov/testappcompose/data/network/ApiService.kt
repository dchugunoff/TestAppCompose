package com.chugunov.testappcompose.data.network

import com.google.gson.Gson

class ApiService {

    /**
     * Класс ApiService, в котором имитируется загрузка данных
     */

    private val json = """
        {
            "users": [
                {
                    "name": "Misha",
                    "age": "20"
                },
                {
                    "name": "Dmitry",
                    "age": "21"
                },
                {
                    "name": "Elena",
                    "age": "18"
                },
                {
                    "name": "Pavel",
                    "age": "25"
                }
            ]
        }
    """.trimIndent()

    //Функция для парсинга JSON в лист UserDto с помощью библиотеки GSON.
    fun getUsersListDto(): UsersDto {
        return Gson().fromJson(json, UsersDto::class.java)
    }
}