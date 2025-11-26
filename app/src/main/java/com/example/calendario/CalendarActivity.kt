package com.example.calendario

import adapters.ReminderAdapter
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendario.data.AppDatabase
import com.example.calendario.data.models.Reminder
import com.example.calendario.databinding.ActivityCalendarBinding
import com.example.calendario.utils.NotificationHelper
import com.example.calendario.utils.SessionManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding
    private lateinit var database: AppDatabase
    private lateinit var sessionManager: SessionManager
    private lateinit var notificationHelper: NotificationHelper
    private lateinit var adapter: ReminderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        database = AppDatabase.getDatabase(this)
        sessionManager = SessionManager(this)
        notificationHelper = NotificationHelper(this)

        setupRecyclerView()
        loadReminders()

        binding.fabAddReminder.setOnClickListener {
            showAddReminderDialog()
        }
    }

    private fun setupRecyclerView() {
        adapter = ReminderAdapter(
            onDeleteClick = { reminder ->
                deleteReminder(reminder)
            },
            onCompleteClick = { reminder ->
                toggleReminderCompletion(reminder)
            }
        )

        binding.rvReminders.layoutManager = LinearLayoutManager(this)
        binding.rvReminders.adapter = adapter
    }

    private fun loadReminders() {
        val userId = sessionManager.getUserId()
        database.reminderDao().getRemindersByUser(userId).observe(this) { reminders ->
            adapter.submitList(reminders)
        }
    }

    private fun showAddReminderDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_reminder, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etTitle)
        val etDescription = dialogView.findViewById<EditText>(R.id.etDescription)

        val selectedDateTime = Calendar.getInstance()

        val btnSelectDate = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSelectDate)
        val btnSelectTime = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSelectTime)

        btnSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    selectedDateTime.set(year, month, day)
                    updateDateTimeButton(btnSelectDate, selectedDateTime)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnSelectTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                this,
                { _, hour, minute ->
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hour)
                    selectedDateTime.set(Calendar.MINUTE, minute)
                    updateDateTimeButton(btnSelectTime, selectedDateTime)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        AlertDialog.Builder(this)
            .setTitle("Nuevo Recordatorio")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val title = etTitle.text.toString().trim()
                val description = etDescription.text.toString().trim()

                if (title.isNotEmpty()) {
                    saveReminder(title, description, selectedDateTime.timeInMillis)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun updateDateTimeButton(button: com.google.android.material.button.MaterialButton, calendar: Calendar) {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        button.text = format.format(calendar.time)
    }

    private fun saveReminder(title: String, description: String, dateTime: Long) {
        lifecycleScope.launch {
            val userId = sessionManager.getUserId()
            val notificationId = System.currentTimeMillis().toInt()

            val reminder = Reminder(
                userId = userId,
                title = title,
                description = description,
                dateTime = dateTime,
                notificationId = notificationId
            )

            database.reminderDao().insert(reminder)

            if (dateTime > System.currentTimeMillis()) {
                notificationHelper.scheduleReminder(notificationId, title, dateTime)
            }
        }
    }

    private fun deleteReminder(reminder: Reminder) {
        lifecycleScope.launch {
            database.reminderDao().delete(reminder)
            notificationHelper.cancelReminder(reminder.notificationId)
        }
    }

    private fun toggleReminderCompletion(reminder: Reminder) {
        lifecycleScope.launch {
            val updatedReminder = reminder.copy(isCompleted = !reminder.isCompleted)
            database.reminderDao().update(updatedReminder)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_calendar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}