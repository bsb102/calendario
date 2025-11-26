package com.example.calendario

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.calendario.data.AppDatabase
import com.example.calendario.data.dao.UserDao
import com.example.calendario.data.models.User
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class UserDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetUser() = runBlocking {
        val user = User(id = 1, username = "testuser", password = "password")
        userDao.insert(user)
        val userFromDb = userDao.getUserById(1)
        assertNotNull(userFromDb)
        assertEquals(user.username, userFromDb?.username)
    }

    @Test
    @Throws(Exception::class)
    fun updateUserAndGet() = runBlocking {
        val user = User(id = 1, username = "testuser", password = "password")
        userDao.insert(user)

        val updatedUser = user.copy(username = "newusername")
        userDao.update(updatedUser)

        val userFromDb = userDao.getUserById(1)
        assertNotNull(userFromDb)
        assertEquals("newusername", userFromDb?.username)
    }
}