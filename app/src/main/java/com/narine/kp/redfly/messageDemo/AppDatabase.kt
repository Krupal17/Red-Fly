package com.narine.kp.redfly.messageDemo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SmsEntity::class, ChatThread::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun smsDao(): SmsDao
    abstract fun chatThreadDao(): ChatThreadDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sms_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}
