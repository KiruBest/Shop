package com.shop.domain.firebase

import com.shop.domain.models.CurrentUser

interface IUserDatabase {
    fun writeUser(uid: String, currentUser: CurrentUser): Boolean
    fun signOut(): CurrentUser
    fun readCurrentUser(uid: String, getCurrentUserCallback: GetCurrentUserCallback): CurrentUser
    fun signIn(email: String, password: String, callback: BooleanCallback): Boolean
    fun createAccount(email: String, password: String, callback: BooleanCallback): Boolean
}