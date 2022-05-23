package com.shop.firebase

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shop.models.MessageModel
import com.shop.ui.admin.ADMIN_ID

open class MessageDatabase : IMessageDatabase {
    override fun sendMessage(message: MessageModel) {
        val id = "${message.userID}_${message.date}"

        message.chat_id = id
        Firebase.database.reference.child(getMessageReference()).child(id).setValue(message)
    }

    override fun getAllMessage(callback: (MutableList<MessageModel>) -> Unit) {
        val messages: MutableList<MessageModel> = mutableListOf()
        Firebase.database.getReference(getMessageReference())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val message: MessageModel = it.getValue(MessageModel::class.java)!!
                        message.chat_id = it.key!!
                        if (!messages.contains(message)) {
                            if (Firebase.auth.currentUser?.uid == ADMIN_ID) {
                                messages.add(message)
                            } else if (message.userID == Firebase.auth.currentUser?.uid || message.chat_id.contains(
                                    Firebase.auth.currentUser?.uid!!
                                )) {
                                messages.add(message)
                            }
                        }
                    }
                    callback.invoke(messages)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })
    }

    private fun getMessageReference() = "message"
}

interface IMessageDatabase {
    fun sendMessage(message: MessageModel)
    fun getAllMessage(callback: (MutableList<MessageModel>) -> Unit)
}

object MessageDatabaseObject : MessageDatabase()