package com.example.calendario.data.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendario.data.AppDatabase
import com.example.calendario.data.models.User
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertUser_and_getUserById() = runBlocking {
        val user = User(id = 1, username = "testuser", password = "password")
        userDao.insert(user)
        val userFromDb = userDao.getUserById(1)
        assertThat(userFromDb).isNotNull()
        assertThat(userFromDb?.username).isEqualTo("testuser")
    }

    @Test
    fun updateUser_and_getUsername() = runBlocking {
        val user = User(id = 2, username = "originalName", password = "password")
        userDao.insert(user)
        val updatedUser = user.copy(username = "newName")
        userDao.update(updatedUser)
        val userFromDb = userDao.getUserById(2)
        assertThat(userFromDb?.username).isEqualTo("newName")
    }
}