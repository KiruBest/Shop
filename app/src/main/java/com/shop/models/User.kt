package com.shop.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//Класс хранит информацию о конкретном пользователе
@Parcelize
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
    var age: Int? = null,
    var uid: String = ""
): Parcelable {
    companion object {
        var currentUser: User? = null
    }
}
