package com.shop.models

data class MessageModel(
    val message: String = "",
    val userID: String = "",
    val date: Long = -1,
    var chat_id: String = "",
    var adminSendCheck: Boolean = false
)
