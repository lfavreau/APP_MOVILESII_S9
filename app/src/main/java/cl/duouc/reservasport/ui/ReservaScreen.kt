package cl.duouc.reservasport.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cl.duouc.reservasport.data.Reserva

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaScreen(viewModel: ReservaViewModel) {
    val reservas by viewModel.reservas.collectAsState()
    var mostrarDialogo by remember { mutableStateOf(false) }
    var reservaSeleccionada by remember { mutableStateOf<Reserva?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ReservaSport") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D47A1),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    reservaSeleccionada = null
                    mostrarDialogo = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = "Agregar reserva") },
                text = { Text("Nueva reserva") },
                containerColor = Color(0xFF0D47A1),
                contentColor = Color.White
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Reservas deportivas",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "CRUD local.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (reservas.isEmpty()) {
                Text(
                    text = "No hay reservas registradas. Presiona “Nueva reserva” para agregar una.",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn {
                    items(reservas) { reserva ->
                        ReservaCard(
                            reserva = reserva,
                            onEditar = {
                                reservaSeleccionada = reserva
                                mostrarDialogo = true
                            },
                            onEliminar = {
                                viewModel.eliminarReserva(reserva)
                            }
                        )
                    }
                }
            }
        }

        if (mostrarDialogo) {
            ReservaDialog(
                reserva = reservaSeleccionada,
                onCerrar = { mostrarDialogo = false },
                onGuardar = { nombre, cancha, fecha, hora, estado ->
                    if (reservaSeleccionada == null) {
                        viewModel.guardarReserva(nombre, cancha, fecha, hora, estado)
                    } else {
                        viewModel.actualizarReserva(
                            reservaSeleccionada!!,
                            nombre,
                            cancha,
                            fecha,
                            hora,
                            estado
                        )
                    }
                    mostrarDialogo = false
                }
            )
        }
    }
}

@Composable
fun ReservaCard(
    reserva: Reserva,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = reserva.nombreUsuario,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text("Cancha: ${reserva.cancha}")
            Text("Fecha: ${reserva.fecha}")
            Text("Hora: ${reserva.hora}")
            Text("Estado: ${reserva.estado}")

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                Button(onClick = onEditar) {
                    Text("Editar reserva")
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(onClick = onEliminar) {
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
fun ReservaDialog(
    reserva: Reserva?,
    onCerrar: () -> Unit,
    onGuardar: (String, String, String, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf(reserva?.nombreUsuario ?: "") }
    var cancha by remember { mutableStateOf(reserva?.cancha ?: "") }
    var fecha by remember { mutableStateOf(reserva?.fecha ?: "") }
    var hora by remember { mutableStateOf(reserva?.hora ?: "") }
    var estado by remember { mutableStateOf(reserva?.estado ?: "Pendiente") }
    var error by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = {
            Text(if (reserva == null) "Nueva reserva" else "Editar reserva")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del usuario") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = cancha,
                    onValueChange = { cancha = it },
                    label = { Text("Cancha") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha") },
                    placeholder = { Text("Ej: 01/06/2026") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = hora,
                    onValueChange = { hora = it },
                    label = { Text("Hora") },
                    placeholder = { Text("Ej: 18:00") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = estado,
                    onValueChange = { estado = it },
                    label = { Text("Estado") },
                    placeholder = { Text("Pendiente o Confirmada") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (error.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (
                        nombre.isBlank() ||
                        cancha.isBlank() ||
                        fecha.isBlank() ||
                        hora.isBlank() ||
                        estado.isBlank()
                    ) {
                        error = "Completa todos los campos antes de guardar."
                    } else {
                        onGuardar(nombre, cancha, fecha, hora, estado)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onCerrar) {
                Text("Cancelar")
            }
        }
    )
}