Aplicación de Recordatorios - Android
Autor Braulio Sánchez

Descripción del Proyecto

Aplicación móvil Android desarrollada en Kotlin utilizando Android Studio que permite a los usuarios gestionar sus recordatorios de manera eficiente. La aplicación cuenta con un sistema completo de autenticación, almacenamiento local seguro, soporte avanzado para el ciclo de vida de la aplicación y notificaciones programadas para nunca olvidar tareas importantes.

<hr>

Funcionalidades Implementadas

Autenticación y Seguridad
Sistema de Registro de Usuarios   - Validación de campos (nombre de usuario y contraseña)   - Verificación de contraseñas coincidentes   - Contraseña mínima de 6 caracteres   - Prevención de usuarios duplicados

Sistema de Inicio de Sesión   - Autenticación segura con credenciales   - Sesión persistente usando EncryptedSharedPreferences   - Cierre de sesión disponible desde el menú

Gestión de Recordatorios
Crear Recordatorios   - Título del recordatorio   - Descripción opcional   - Selector de fecha   - Selector de hora   - Almacenamiento en base de datos local (Room)

Recordatorios Recurrentes (NUEVO)   - Opción de configurar repetición diaria, semanal o mensual.   - Manejo de series de recordatorios en la base de datos.

Visualizar Recordatorios   - Lista ordenada por fecha y hora   - Diseño Material Design con CardViews   - Muestra título, descripción y fecha/hora

Completar Recordatorios   - Checkbox para marcar como completado   - Texto tachado visual para recordatorios completados   - Estado persistente

Eliminar Recordatorios   - Botón de eliminación en cada recordatorio   - Cancela automáticamente la notificación asociada

Sistema de Notificaciones
Solicitud de Permisos   - Solicitud dinámica de permiso POST_NOTIFICATIONS (Android 13+)   - Manejo de permisos de alarmas exactas

Notificaciones Programadas   - AlarmManager para programar notificaciones exactas   - Notificaciones en la hora programada   - Canal de notificaciones dedicado   - Iconos y diseño personalizado

Almacenamiento de Datos
Base de Datos Local (Room)   - Tabla de usuarios con credenciales   - Tabla de recordatorios con relación a usuarios   - Consultas optimizadas con LiveData

Almacenamiento Seguro   - EncryptedSharedPreferences para la sesión   - Cifrado AES256_GCM   - Protección de datos sensibles

<hr>

Arquitectura y Tecnologías

Lenguaje y Framework
Kotlin - Lenguaje de programación principal

Android Studio - IDE de desarrollo

Librerías y Componentes
Room Database

Hilt (Inyección de Dependencias)

Coroutines y Kotlin Flow (StateFlow/SharedFlow)

ViewBinding

Material Design 3

EncryptedSharedPreferences

Lifecycle Components

Arquitectura
MVVM (Model-View-ViewModel) con manejo de estado reactivo (StateFlow)

Repository Pattern con DAOs

Separation of Concerns

Inyección de Dependencias para facilitar pruebas unitarias y modularidad.

<hr>

Estructura del Proyecto

com.example.calendario/
├── data/
│   ├── models/
│   │   ├── User.kt
│   │   └── Reminder.kt
│   ├── dao/
│   │   ├── UserDao.kt
│   │   └── ReminderDao.kt
│   └── AppDatabase.kt
├── di/ <--- NUEVO: Módulos de inyección de dependencias (Hilt)
│   └── DatabaseModule.kt
├── adapters/
│   └── ReminderAdapter.kt
├── receivers/
│   └── ReminderReceiver.kt
├── utils/
│   ├── SessionManager.kt
│   └── NotificationHelper.kt
├── viewmodels/ <--- NUEVO: Para organizar los ViewModels
│   ├── AuthViewModel.kt
│   └── ReminderViewModel.kt
├── MainActivity.kt
├── LoginActivity.kt
├── RegisterActivity.kt
└── CalendarActivity.kt
<hr>

Pasos para Ejecutar el Proyecto

Requisitos Previos
Android Studio Hedgehog (2023.1.1) o superior

JDK 17 o superior

SDK mínimo: API 26

SDK objetivo: API 34

Instalación
Clonar el proyecto:

Bash

git clone <url-del-repositorio>
cd calendario-app
Abrir en Android Studio

Verificar settings.gradle.kts:

Kotlin

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
<hr>

Creador

Braulio Sánchez
