package com.shop.data.firebase

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shop.domain.firebase.BooleanCallback
import com.shop.domain.firebase.GetCurrentUserCallback
import com.shop.domain.firebase.IUserDatabase
import com.shop.domain.models.CurrentUser

class UserDatabase(private val auth: FirebaseAuth) : IUserDatabase {
    //Информация о пользователе записывается в ДБ
    override fun writeUser(uid: String, currentUser: CurrentUser): Boolean {
        Firebase.database.reference.child("users").child(uid)
            .setValue(currentUser)
        return true
    }

    //Текущий пользователь ищется в БД
    override fun readCurrentUser(uid: String, callback: GetCurrentUserCallback): CurrentUser {
        var currentUser = CurrentUser()

        Firebase.database.getReference("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        if (it.key == uid) {
                            currentUser = it.getValue(CurrentUser::class.java)!!
                            callback.onCallback(currentUser)
                            return
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                    callback.onCallback(currentUser)
                }

            })

        return currentUser
    }

    //Авторизация
    override fun signIn(email: String, password: String, callback: BooleanCallback): Boolean {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    callback.onCallback(true)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    callback.onCallback(false)
                }
            }

        return false
    }

    //Выход
    override fun signOut(): CurrentUser {
        Firebase.auth.signOut()
        return CurrentUser()
    }

    //Регистрация
    override fun createAccount(email: String, password: String, callback: BooleanCallback): Boolean {
        // [START create_user_with_email]
        val status = false
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    callback.onCallback(this.writeUser(auth.currentUser?.uid.toString(), CurrentUser(email = email)))
                }
                else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    callback.onCallback(false)
                }
            }

        return status
    }
}