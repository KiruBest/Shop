package com.shop.models

//Класс хранит информацию о конкретном пользователе
data class User(
    var name: String = "",
    var lastname: String = "",
    var email: String = "",
    var photo: String = "",
    var login: String = "",
    var street: String = "",
    var home: String = "",
    var entrance: String = "",
    var flat: String = "",
    var age: Int? = null
) {
    companion object {
        var currentUser: User? = null
    }
}
