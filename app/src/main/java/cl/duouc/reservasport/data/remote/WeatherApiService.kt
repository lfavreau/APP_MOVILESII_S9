package cl.duouc.reservasport.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("v1/forecast")
    suspend fun obtenerClima(
        @Query("latitude")
        latitude: Double = -36.827,

        @Query("longitude")
        longitude: Double = -73.0498,

        @Query("current")
        current: String =
            "temperature_2m," +
                    "precipitation," +
                    "rain," +
                    "wind_speed_10m," +
                    "relative_humidity_2m",

        @Query("daily")
        daily: String =
            "temperature_2m_max," +
                    "temperature_2m_min," +
                    "rain_sum," +
                    "precipitation_sum," +
                    "precipitation_hours," +
                    "precipitation_probability_max," +
                    "wind_speed_10m_max",

        @Query("timezone")
        timezone: String = "auto",

        @Query("forecast_days")
        forecastDays: Int = 1
    ): WeatherResponse
}