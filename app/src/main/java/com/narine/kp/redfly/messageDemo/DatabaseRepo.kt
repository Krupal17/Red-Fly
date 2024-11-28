package com.narine.kp.redfly.messageDemo

import android.content.Context
import android.util.Log

class DatabaseRepo {

    lateinit var dbRef:AppDatabase

    fun initDatabase(context: Context){
        dbRef = AppDatabase.getInstance(context)
    }

    suspend fun insertAll(smsList: List<SmsEntity>) {
        dbRef.smsDao().insertAll(smsList)
    }

    suspend fun insertSms(sms: SmsEntity) {
        Log.e("MEllomn", "insertSms: ${sms.address}", )
        dbRef.smsDao().insertSms(sms)
    }

    suspend fun getAllSms(): List<SmsEntity> {
        return dbRef.smsDao().getAllMessages()
    }

    suspend fun getMessagesForAddress(address: String): List<SmsEntity> {
        return dbRef.smsDao().getMessagesForAddress(address)
    }

    suspend fun insertThread(thread: ChatThread) {
        dbRef.chatThreadDao().insertThread(thread)
    }

    suspend fun getAllThreads(): List<ChatThread> {
        return dbRef.chatThreadDao().getAllThreads()
    }


}