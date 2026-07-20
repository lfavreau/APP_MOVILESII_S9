package cl.duouc.reservasport.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InicioScreen(
    emailUsuario: String,
    weatherUiState: WeatherUiState,
    onActualizarClima: () -> Unit,
    onSimularAlertaLluvia: () -> Unit,
    onVerReservas: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(24.dp),
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = "ReservaSport",
            style =
                MaterialTheme.typography.headlineLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Gestión de reservas deportivas",
            style =
                MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = emailUsuario,
            style =
                MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        WeatherCard(
            uiState = weatherUiState,
            onActualizar = onActualizarClima,
            onSimularAlertaLluvia =
                onSimularAlertaLluvia
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = onVerReservas,
            modifier =
                Modifier.fillMaxWidth()
        ) {
            Text("Ver reservas")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedButton(
            onClick = onCerrarSesion,
            modifier =
                Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
private fun WeatherCard(
    uiState: WeatherUiState,
    onActualizar: () -> Unit,
    onSimularAlertaLluvia: () -> Unit
) {
    Card(
        modifier =
            Modifier.fillMaxWidth(),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
    ) {
        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {
            Text(
                text = "Clima en Concepción",
                style =
                    MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text("Consultando clima...")
                }

                uiState.error.isNotBlank() -> {
                    Text(
                        text = uiState.error,
                        color =
                            MaterialTheme.colorScheme.error
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    OutlinedButton(
                        onClick = onActualizar,
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {
                        Text("Reintentar")
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Button(
                        onClick =
                            onSimularAlertaLluvia,
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {
                        Text("Probar alerta de lluvia")
                    }
                }

                else -> {
                    Text(
                        text =
                            "Temperatura actual: " +
                                    uiState.temperatura
                    )

                    Text(
                        text =
                            "Humedad: " +
                                    uiState.humedad
                    )

                    Text(
                        text =
                            "Precipitación actual: " +
                                    uiState.precipitacionActual
                    )

                    Text(
                        text =
                            "Lluvia actual: " +
                                    uiState.lluviaActual
                    )

                    Text(
                        text =
                            "Viento actual: " +
                                    uiState.vientoActual
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "Máxima: " +
                                    uiState.temperaturaMaxima
                    )

                    Text(
                        text =
                            "Mínima: " +
                                    uiState.temperaturaMinima
                    )

                    Text(
                        text =
                            "Probabilidad de lluvia: " +
                                    uiState.probabilidadLluvia
                    )

                    Text(
                        text =
                            "Precipitación total: " +
                                    uiState.precipitacionTotal
                    )

                    Text(
                        text =
                            "Horas con precipitación: " +
                                    uiState.horasPrecipitacion
                    )

                    Text(
                        text =
                            "Viento máximo: " +
                                    uiState.vientoMaximo
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = uiState.recomendacion,
                        style =
                            MaterialTheme.typography.titleSmall
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "Actualización: " +
                                    uiState.ultimaActualizacion,
                        style =
                            MaterialTheme.typography.bodySmall
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    OutlinedButton(
                        onClick = onActualizar,
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {
                        Text("Actualizar clima")
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Button(
                        onClick =
                            onSimularAlertaLluvia,
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {
                        Text("Probar alerta de lluvia")
                    }
                }
            }
        }
    }
}