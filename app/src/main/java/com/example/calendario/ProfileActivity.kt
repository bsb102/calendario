package com.example.calendario

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import com.example.calendario.data.AppDatabase
import com.example.calendario.ui.theme.CalendarioTheme
import com.example.calendario.ui.theme.Theme
import com.example.calendario.utils.SessionManager
import kotlinx.coroutines.launch

class ProfileActivity : ComponentActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        database = AppDatabase.getDatabase(this)

        setContent {
            val context = LocalContext.current
            val currentTheme = sessionManager.getTheme()
            var imageUri by remember { mutableStateOf<Uri?>(null) }

            val galleryLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent(),
                onResult = { uri -> uri?.let { imageUri = it } }
            )

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    if (isGranted) {
                        galleryLauncher.launch("image/*")
                    } else {
                        Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            CalendarioTheme(selectedTheme = currentTheme) {
                ProfileScreen(
                    username = sessionManager.getUsername() ?: "",
                    onLogout = {
                        sessionManager.clearSession()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    },
                    onThemeChange = {
                        sessionManager.saveTheme(it)
                        recreate()
                    },
                    onImageChange = {
                        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            Manifest.permission.READ_MEDIA_IMAGES
                        } else {
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        }
                        permissionLauncher.launch(permission)
                    },
                    onUsernameChange = {
                        lifecycleScope.launch {
                            val userId = sessionManager.getUserId()
                            val user = database.userDao().getUserById(userId)
                            if (user != null) {
                                database.userDao().update(user.copy(username = it))
                                sessionManager.saveSession(userId, it) // Actualizar sesión
                                Toast.makeText(context, "Nombre de usuario actualizado", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    onGeminiClick = {
                        context.startActivity(Intent(context, GeminiActivity::class.java))
                    },
                    imageUri = imageUri
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    username: String,
    onLogout: () -> Unit,
    onThemeChange: (Theme) -> Unit,
    onImageChange: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onGeminiClick: () -> Unit,
    imageUri: Uri?
) {
    var newUsername by remember { mutableStateOf(username) }
    var themeMenuExpanded by remember { mutableStateOf(false) }
    val themes = Theme.entries

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = imageUri?.let { rememberAsyncImagePainter(it) } ?: painterResource(id = R.drawable.ic_profile),
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .clickable { onImageChange() },
            contentScale = ContentScale.Crop
        )

        OutlinedTextField(
            value = newUsername,
            onValueChange = { newUsername = it },
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = { onUsernameChange(newUsername) }) {
            Text("Guardar nombre")
        }

        Box {
            Button(onClick = { themeMenuExpanded = true }) {
                Text("Cambiar Tema")
            }
            DropdownMenu(expanded = themeMenuExpanded, onDismissRequest = { themeMenuExpanded = false }) {
                themes.forEach { theme ->
                    DropdownMenuItem(text = { Text(theme.name) }, onClick = {
                        onThemeChange(theme)
                        themeMenuExpanded = false
                    })
                }
            }
        }

        Button(onClick = onGeminiClick) {
            Text("Consultar a Gemini")
        }

        Spacer(modifier = Modifier.weight(1.0f))

        Button(onClick = onLogout) {
            Text("Cerrar Sesión")
        }
    }
}
