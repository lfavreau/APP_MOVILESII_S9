package cl.duouc.reservasport.data.remote

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val elevation: Double,
    val timezone: String,

    @SerializedName("timezone_abbreviation")
    val timezoneAbbreviation: String,

    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int,

    @SerializedName("current_units")
    val currentUnits: CurrentUnits,

    val current: CurrentWeather,

    @SerializedName("daily_units")
    val dailyUnits: DailyUnits,

    val daily: DailyWeather
)

data class CurrentWeather(
    val time: String,
    val interval: Int,

    @SerializedName("temperature_2m")
    val temperature: Double,

    val precipitation: Double,

    val rain: Double,

    @SerializedName("wind_speed_10m")
    val windSpeed: Double,

    @SerializedName("relative_humidity_2m")
    val relativeHumidity: Double
)

data class CurrentUnits(
    val time: String,
    val interval: String,

    @SerializedName("temperature_2m")
    val temperature: String,

    val precipitation: String,

    val rain: String,

    @SerializedName("wind_speed_10m")
    val windSpeed: String,

    @SerializedName("relative_humidity_2m")
    val relativeHumidity: String
)

data class DailyWeather(
    val time: List<String>,

    @SerializedName("temperature_2m_max")
    val temperatureMax: List<Double>,

    @SerializedName("temperature_2m_min")
    val temperatureMin: List<Double>,

    @SerializedName("rain_sum")
    val rainSum: List<Double>,

    @SerializedName("precipitation_sum")
    val precipitationSum: List<Double>,

    @SerializedName("precipitation_hours")
    val precipitationHours: List<Double>,

    @SerializedName("precipitation_probability_max")
    val precipitationProbabilityMax: List<Int>,

    @SerializedName("wind_speed_10m_max")
    val windSpeedMax: List<Double>
)

data class DailyUnits(
    val time: String,

    @SerializedName("temperature_2m_max")
    val temperatureMax: String,

    @SerializedName("temperature_2m_min")
    val temperatureMin: String,

    @SerializedName("rain_sum")
    val rainSum: String,

    @SerializedName("precipitation_sum")
    val precipitationSum: String,

    @SerializedName("precipitation_hours")
    val precipitationHours: String,

    @SerializedName("precipitation_probability_max")
    val precipitationProbabilityMax: String,

    @SerializedName("wind_speed_10m_max")
    val windSpeedMax: String
)