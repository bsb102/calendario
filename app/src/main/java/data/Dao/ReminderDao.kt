package data.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import data.models.Reminder

@Dao
interface ReminderDao {
    @Insert
    suspend fun insert(reminder: Reminder): Long

    @Update
    suspend fun update(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)

    @Query("SELECT * FROM reminders WHERE userId = :userId ORDER BY dateTime ASC")
    fun getRemindersByUser(userId: Int): LiveData<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE userId = :userId AND dateTime >= :startDate AND dateTime < :endDate ORDER BY dateTime ASC")
    fun getRemindersByDateRange(userId: Int, startDate: Long, endDate: Long): LiveData<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE id = :id")
    suspend fun getReminderById(id: Int): Reminder?
}