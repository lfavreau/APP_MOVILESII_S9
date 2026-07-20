package cl.duouc.reservasport.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duouc.reservasport.data.repository.WeatherRepository
import cl.duouc.reservasport.notification.NotificationHelper
import kotlinx.coroutines.launch

data class WeatherUiState(
    val isLoading: Boolean = false,
    val temperatura: String = "",
    val humedad: String = "",
    val precipitacionActual: String = "",
    val lluviaActual: String = "",
    val vientoActual: String = "",
    val temperaturaMaxima: String = "",
    val temperaturaMinima: String = "",
    val lluviaTotal: String = "",
    val precipitacionTotal: String = "",
    val horasPrecipitacion: String = "",
    val probabilidadLluvia: String = "",
    val vientoMaximo: String = "",
    val recomendacion: String = "",
    val ultimaActualizacion: String = "",
    val error: String = ""
)

class WeatherViewModel(
    private val repository: WeatherRepository,
    private val notificationHelper: NotificationHelper
) : ViewModel() {

    var uiState by mutableStateOf(
        WeatherUiState()
    )
        private set

    private var ultimaCondicionNotificada = ""

    init {
        cargarClima()
    }

    fun cargarClima() {
        viewModelScope.launch {
            uiState = WeatherUiState(
                isLoading = true
            )

            try {
                val respuesta =
                    repository.obtenerClimaConcepcion()

                val actual =
                    respuesta.current

                val unidadesActuales =
                    respuesta.currentUnits

                val diario =
                    respuesta.daily

                val unidadesDiarias =
                    respuesta.dailyUnits

                val temperaturaMaxima =
                    diario.temperatureMax
                        .firstOrNull() ?: 0.0

                val temperaturaMinima =
                    diario.temperatureMin
                        .firstOrNull() ?: 0.0

                val lluviaTotal =
                    diario.rainSum
                        .firstOrNull() ?: 0.0

                val precipitacionTotal =
                    diario.precipitationSum
                        .firstOrNull() ?: 0.0

                val horasPrecipitacion =
                    diario.precipitationHours
                        .firstOrNull() ?: 0.0

                val probabilidadLluvia =
                    diario
                        .precipitationProbabilityMax
                        .firstOrNull() ?: 0

                val vientoMaximo =
                    diario.windSpeedMax
                        .firstOrNull() ?: 0.0

                val recomendacion =
                    generarRecomendacion(
                        probabilidadLluvia =
                            probabilidadLluvia,
                        precipitacionTotal =
                            precipitacionTotal,
                        vientoMaximo =
                            vientoMaximo
                    )

                uiState = WeatherUiState(
                    isLoading = false,

                    temperatura =
                        "${actual.temperature} " +
                                unidadesActuales.temperature,

                    humedad =
                        "${actual.relativeHumidity.toInt()} " +
                                unidadesActuales.relativeHumidity,

                    precipitacionActual =
                        "${actual.precipitation} " +
                                unidadesActuales.precipitation,

                    lluviaActual =
                        "${actual.rain} " +
                                unidadesActuales.rain,

                    vientoActual =
                        "${actual.windSpeed} " +
                                unidadesActuales.windSpeed,

                    temperaturaMaxima =
                        "$temperaturaMaxima " +
                                unidadesDiarias.temperatureMax,

                    temperaturaMinima =
                        "$temperaturaMinima " +
                                unidadesDiarias.temperatureMin,

                    lluviaTotal =
                        "$lluviaTotal " +
                                unidadesDiarias.rainSum,

                    precipitacionTotal =
                        "$precipitacionTotal " +
                                unidadesDiarias.precipitationSum,

                    horasPrecipitacion =
                        "$horasPrecipitacion " +
                                unidadesDiarias.precipitationHours,

                    probabilidadLluvia =
                        "$probabilidadLluvia " +
                                unidadesDiarias
                                    .precipitationProbabilityMax,

                    vientoMaximo =
                        "$vientoMaximo " +
                                unidadesDiarias.windSpeedMax,

                    recomendacion =
                        recomendacion,

                    ultimaActualizacion =
                        actual.time,

                    error = ""
                )

                evaluarNotificacion(
                    probabilidadLluvia =
                        probabilidadLluvia,
                    precipitacionTotal =
                        precipitacionTotal,
                    vientoMaximo =
                        vientoMaximo
                )
            } catch (exception: Exception) {
                uiState = WeatherUiState(
                    isLoading = false,
                    error =
                        "No fue posible obtener el clima. " +
                                "Revisa tu conexión."
                )
            }
        }
    }

    fun simularAlertaLluvia() {
        notificationHelper.mostrarAlertaClimatica(
            titulo = "Alerta de lluvia de prueba",
            mensaje =
                "Se detectó una señal simulada de lluvia intensa " +
                        "en Concepción. Considera reservar una cancha cubierta."
        )
    }

    private fun generarRecomendacion(
        probabilidadLluvia: Int,
        precipitacionTotal: Double,
        vientoMaximo: Double
    ): String {
        return when {
            probabilidadLluvia >= 70 ||
                    precipitacionTotal >= 5.0 -> {
                "Alta probabilidad de lluvia. " +
                        "Se recomienda una cancha cubierta."
            }

            vientoMaximo >= 40.0 -> {
                "Se esperan vientos fuertes. " +
                        "Se recomienda realizar la actividad en interior."
            }

            probabilidadLluvia >= 40 -> {
                "Existe posibilidad de lluvia. " +
                        "Revisa nuevamente el clima antes de salir."
            }

            else -> {
                "Condiciones adecuadas para actividades deportivas."
            }
        }
    }

    private fun evaluarNotificacion(
        probabilidadLluvia: Int,
        precipitacionTotal: Double,
        vientoMaximo: Double
    ) {
        val condicion =
            when {
                probabilidadLluvia >= 70 ||
                        precipitacionTotal >= 5.0 -> {
                    "lluvia"
                }

                vientoMaximo >= 40.0 -> {
                    "viento"
                }

                else -> {
                    ""
                }
            }

        if (
            condicion.isBlank() ||
            condicion == ultimaCondicionNotificada
        ) {
            return
        }

        ultimaCondicionNotificada = condicion

        when (condicion) {
            "lluvia" -> {
                notificationHelper.mostrarAlertaClimatica(
                    titulo = "Alerta de lluvia",
                    mensaje =
                        "Se esperan condiciones lluviosas en Concepción. " +
                                "Considera reservar una cancha cubierta."
                )
            }

            "viento" -> {
                notificationHelper.mostrarAlertaClimatica(
                    titulo = "Alerta de viento",
                    mensaje =
                        "Se esperan vientos fuertes en Concepción. " +
                                "Evalúa realizar la actividad deportiva en interior."
                )
            }
        }
    }
}