package cl.duouc.reservasport

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import cl.duouc.reservasport.data.ReservaDatabase
import cl.duouc.reservasport.data.remote.RetrofitClient
import cl.duouc.reservasport.data.repository.ReservaRepository
import cl.duouc.reservasport.data.repository.WeatherRepository
import cl.duouc.reservasport.data.session.SessionManager
import cl.duouc.reservasport.navigation.AppNavigation
import cl.duouc.reservasport.notification.NotificationHelper
import cl.duouc.reservasport.ui.ReservaViewModel
import cl.duouc.reservasport.ui.ReservaViewModelFactory
import cl.duouc.reservasport.ui.WeatherViewModel
import cl.duouc.reservasport.ui.WeatherViewModelFactory
import cl.duouc.reservasport.ui.theme.ReservaSportTheme

class MainActivity :
    ComponentActivity() {

    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts
                .RequestPermission()
        ) {
        }

    private val notificationHelper by lazy {
        NotificationHelper(
            applicationContext
        )
    }

    private val reservaViewModel:
            ReservaViewModel by viewModels {

        val dao =
            ReservaDatabase
                .obtenerDatabase(
                    applicationContext
                )
                .reservaDao()

        val repository =
            ReservaRepository(
                dao
            )

        ReservaViewModelFactory(
            repository
        )
    }

    private val weatherViewModel:
            WeatherViewModel by viewModels {

        val repository =
            WeatherRepository(
                RetrofitClient
                    .weatherApiService
            )

        WeatherViewModelFactory(
            repository =
                repository,
            notificationHelper =
                notificationHelper
        )
    }

    override fun onCreate(
        savedInstanceState:
        Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        notificationHelper
            .crearCanal()

        solicitarPermisoNotificaciones()

        val sessionManager =
            SessionManager(
                applicationContext
            )

        setContent {
            ReservaSportTheme {
                AppNavigation(
                    reservaViewModel =
                        reservaViewModel,
                    weatherViewModel =
                        weatherViewModel,
                    sessionManager =
                        sessionManager
                )
            }
        }
    }

    private fun solicitarPermisoNotificaciones() {
        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU
        ) {
            notificationPermissionLauncher
                .launch(
                    Manifest.permission
                        .POST_NOTIFICATIONS
                )
        }
    }
}