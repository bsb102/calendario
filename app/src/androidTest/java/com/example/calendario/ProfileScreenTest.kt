package com.example.calendario

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.calendario.ui.theme.CalendarioTheme
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun profileScreen_displaysUsernameAndButtons() {
        // Lanza el Composable con datos de prueba
        composeTestRule.setContent {
            CalendarioTheme {
                ProfileScreen(
                    username = "testuser",
                    onLogout = {},
                    onThemeChange = {},
                    onImageChange = {},
                    onUsernameChange = {},
                    onGeminiClick = {},
                    imageUri = null
                )
            }
        }

        // Verifica que los elementos principales se muestren
        composeTestRule.onNodeWithText("testuser").assertIsDisplayed()
        composeTestRule.onNodeWithText("Guardar nombre").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cambiar Tema").assertIsDisplayed()
        composeTestRule.onNodeWithText("Consultar a Gemini").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cerrar Sesión".uppercase()).assertIsDisplayed()
    }

    @Test
    fun changeThemeButton_opensDropdownMenu() {
        // Lanza el Composable
        composeTestRule.setContent {
            CalendarioTheme {
                ProfileScreen(
                    username = "testuser",
                    onLogout = {},
                    onThemeChange = {},
                    onImageChange = {},
                    onUsernameChange = {},
                    onGeminiClick = {},
                    imageUri = null
                )
            }
        }

        // Simula un clic en el botón para cambiar el tema
        composeTestRule.onNodeWithText("Cambiar Tema").performClick()

        // Verifica que el menú desplegable (y al menos una de sus opciones) se muestre
        composeTestRule.onNodeWithText("DEFAULT").assertIsDisplayed()
    }
}
