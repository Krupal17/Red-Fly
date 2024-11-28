package com.narine.kp.redfly.messageDemo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SmsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSms(sms:SmsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(smsList: List<SmsEntity>)

    @Query("SELECT * FROM SmsEntity WHERE address = :address ORDER BY date DESC")
    suspend fun getMessagesForAddress(address: String): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity ORDER BY date DESC")
    suspend fun getAllMessages(): List<SmsEntity>

}

@Dao
interface ChatThreadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThread(thread: ChatThread)

    @Query("SELECT * FROM ChatThread ORDER BY lastMessageDate DESC")
    suspend fun getAllThreads(): List<ChatThread>
}
