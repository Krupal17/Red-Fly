package com.narine.kp.redfly.messageDemo

data class SmsMessage(
    val id: Long,
    val address: String,
    val date: Long,
    val body: String
)
