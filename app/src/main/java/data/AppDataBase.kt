package com.example.calendario.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.calendario.data.dao.ReminderDao
import com.example.calendario.data.dao.UserDao
import com.example.calendario.data.models.Reminder
import com.example.calendario.data.models.User

@Database(entities = [User::class, Reminder::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "calendario_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}