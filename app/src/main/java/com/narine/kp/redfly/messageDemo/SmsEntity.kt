package com.narine.kp.redfly.messageDemo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SmsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val address: String,
    val date: Long,
    val body: String
)

@Entity
data class ChatThread(
    @PrimaryKey(autoGenerate = true) val threadId: Long = 0,
    val address: String,
    val lastMessageDate: Long
)
