package cl.duouc.reservasport.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cl.duouc.reservasport.data.repository.WeatherRepository
import cl.duouc.reservasport.notification.NotificationHelper

class WeatherViewModelFactory(
    private val repository: WeatherRepository,
    private val notificationHelper: NotificationHelper
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (
            modelClass.isAssignableFrom(
                WeatherViewModel::class.java
            )
        ) {
            return WeatherViewModel(
                repository =
                    repository,
                notificationHelper =
                    notificationHelper
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido: ${modelClass.name}"
        )
    }
}