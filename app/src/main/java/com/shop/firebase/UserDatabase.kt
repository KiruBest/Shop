package com.shop.firebase

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shop.models.User
import com.shop.ui.main.MainActivity

class UserDatabase private constructor() : IUserDatabase {
    override val auth: FirebaseAuth = Firebase.auth

    //Информация о пользователе записывается в ДБ
    override fun writeUser(uid: String, email: String, name: String, lastname: String) {
        val user = User(email, name, lastname)
        Firebase.database.reference.child("users").child(uid).setValue(user)
    }

    //Текущий пользователь ищется в БД
    override fun readCurrentUser(uid: String, activity: Activity) {
        Firebase.database.getReference("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        if (it.key == uid) {
                            User.currentUser = it.getValue(User::class.java)
                            Log.d(TAG, User.currentUser?.email.toString())
                            if (activity is MainActivity) activity.invalidateOptionsMenu()
                            return
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }

            })
    }

    //Авторизация
    override fun signIn(email: String, password: String, activity: Activity) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    //Выход
    override fun signOut() {
        User.currentUser = null
        Firebase.auth.signOut()
    }

    //Регистрация
    override fun createAccount(email: String, password: String, activity: Activity) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(activity, "Пользователь $email успешно создан",
                        Toast.LENGTH_SHORT).show()
                    auth.currentUser?.uid?.let { this.writeUser(it, email) }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        //Типа singleton
        private var userDatabase: IUserDatabase? = null
        fun instance(): IUserDatabase {
            if (userDatabase == null) userDatabase = UserDatabase()
            return userDatabase as IUserDatabase
        }
    }
}

interface IUserDatabase {
    val auth: FirebaseAuth?

    fun writeUser(uid: String, email: String, name: String = "", lastname: String = "")
    fun signOut()
    fun readCurrentUser(uid: String, activity: Activity)
    fun signIn(email: String, password: String, activity: Activity)
    fun createAccount(email: String, password: String, activity: Activity)
}