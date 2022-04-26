package com.shop.domain.models

//Класс хранит информацию о конкретном пользователе
data class CurrentUser(
    val name: String = "",
    val lastname: String = "",
    val email: String = "",
    val photo: String = ""
)
