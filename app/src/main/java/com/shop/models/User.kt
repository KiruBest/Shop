package com.shop.models

//Класс хранит информацию о конкретном пользователе
data class User(
    val name: String = "",
    val lastname: String = "",
    val email: String = "",
    val photo: String = ""
) {
    companion object {
        var currentUser: User? = null
    }
}
