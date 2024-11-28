package com.narine.kp.redfly.messageDemo

import android.content.Context
import android.net.Uri
import android.util.Log

object Manager {

    lateinit var repoRef: DatabaseRepo

    suspend fun fetchAllSms(context: Context) {
        val smsList = mutableListOf<SmsMessage>()
        val uri = Uri.parse("content://sms")
        val cursor = context.contentResolver.query(
            uri, null,
            null,
            null,
            "date DESC"
        )

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow("_id"))
                val address = it.getString(it.getColumnIndexOrThrow("address"))
                val date = it.getLong(it.getColumnIndexOrThrow("date"))
                val body = it.getString(it.getColumnIndexOrThrow("body"))

                smsList.add(SmsMessage(id, address, date, body))
            }
        }
        Log.e("TAG-->", "fetchAllSms: ${smsList.size}", )
        processMessages(smsList)
    }

    suspend fun processMessages(smsList: List<SmsMessage>) {

        // Save all messages
        val smsEntities = smsList.map { sms ->
            SmsEntity(sms.id, sms.address, sms.date, sms.body)
        }
        repoRef.insertAll(smsEntities)

        // Group messages by address
        val threads = smsEntities.groupBy { it.address }.map { (address, messages) ->
            val lastMessageDate = messages.maxOf { it.date }
            ChatThread(address = address, lastMessageDate = lastMessageDate)
        }

        // Save chat threads
        threads.forEach { thread ->
            repoRef.insertThread(thread)
        }
    }

}