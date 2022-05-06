package com.shop.firebase

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Continuation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.shop.models.User

class UserDatabase private constructor() : IUserDatabase {

    //Информация о пользователе записывается в ДБ
    override fun writeUser(uid: String, user: User) {
        Firebase.database.reference.child("users").child(uid).setValue(user)
    }

    //Текущий пользователь ищется в БД
    override fun readCurrentUser(uid: String, callback: GetUserCallback) {
        Firebase.database.getReference("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        if (it.key == uid) {
                            val user: User = it.getValue(User::class.java)!!
                            callback.callback(user)
                            Log.d(TAG, user.toString())
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
    override fun signIn(email: String, password: String, callback: BooleanAuthUserCallback) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    callback.callback(true)
                    Log.d(TAG, "signInWithEmail:success")
                } else {
                    // If sign in fails, display a message to the user.
                    callback.callback(false)
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                }
            }
    }

    //Выход
    override fun signOut() {
        User.currentUser = null
        Firebase.auth.signOut()
    }

    //Регистрация
    override fun createAccount(email: String, password: String, callback: BooleanAuthUserCallback) {
        // [START create_user_with_email]
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    writeUser(Firebase.auth.currentUser?.uid.toString(), User(email = email))
                    callback.callback(true)
                    Log.d(TAG, "createUserWithEmail:success")
                } else {
                    // If sign in fails, display a message to the user.
                    callback.callback(false)
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                }
            }
    }

    override fun saveProfilePhoto(uid: String, currentUserPhoto: Uri, urlReturnCallback: UrlReturnCallback) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReference("user_photo")
        val downloadImageUri: StorageReference = storageRef.child("${uid}_photo.webm")
        var url: String

        val uploadTask: UploadTask = downloadImageUri.putFile(currentUserPhoto)

        try {
            uploadTask
                .addOnFailureListener {
                    Log.d("uploadInStorageError", it.stackTraceToString())
                }
                .addOnSuccessListener {
                    Log.d("successful", "Successful")

                    uploadTask.continueWithTask(Continuation {
                        return@Continuation downloadImageUri.downloadUrl
                    }).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d("successful", "Successful")

                            url = it.result.toString()

                            urlReturnCallback.callback(url)
                        }
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
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
    fun writeUser(uid: String, user: User)
    fun signOut()
    fun readCurrentUser(uid: String, callback: GetUserCallback)
    fun signIn(email: String, password: String, callback: BooleanAuthUserCallback)
    fun createAccount(email: String, password: String, callback: BooleanAuthUserCallback)
    fun saveProfilePhoto(uid: String, currentUserPhoto: Uri, urlReturnCallback: UrlReturnCallback)
}

interface GetUserCallback {
    fun callback(currentUser: User)
}

interface BooleanAuthUserCallback {
    fun callback(status: Boolean)
}

interface UrlReturnCallback {
    fun callback(url: String)
}