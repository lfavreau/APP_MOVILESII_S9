package cl.duouc.reservasport.data.repository

import cl.duouc.reservasport.data.remote.WeatherApiService
import cl.duouc.reservasport.data.remote.WeatherResponse

class WeatherRepository(
    private val apiService: WeatherApiService
) {

    suspend fun obtenerClimaConcepcion(): WeatherResponse {
        return apiService.obtenerClima()
    }
}