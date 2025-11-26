package data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val password: String // En producción, usa hashing (BCrypt)
)

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val title: String,
    val description: String = "",
    val dateTime: Long, // Timestamp en milisegundos
    val isCompleted: Boolean = false,
    val notificationId: Int = 0
)