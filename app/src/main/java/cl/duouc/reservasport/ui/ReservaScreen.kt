package cl.duouc.reservasport.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cl.duouc.reservasport.data.Reserva
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaScreen(
    viewModel: ReservaViewModel,
    onVolver: () -> Unit
) {
    val reservas by viewModel.reservas.collectAsState()
    val isLoading = viewModel.isLoading
    val mensajeOperacion = viewModel.mensajeOperacion
    val mensajeError = viewModel.mensajeError

    var mostrarDialogo by remember {
        mutableStateOf(false)
    }

    var reservaSeleccionada by remember {
        mutableStateOf<Reserva?>(null)
    }

    var reservaDetalle by remember {
        mutableStateOf<Reserva?>(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("ReservaSport")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onVolver
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D47A1),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    reservaSeleccionada = null
                    mostrarDialogo = true
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar reserva"
                    )
                },
                text = {
                    Text("Nueva reserva")
                },
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

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Selecciona una reserva para ver sus detalles.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            if (isLoading) {
                CircularProgressIndicator()

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }

            if (mensajeOperacion.isNotBlank()) {
                Text(
                    text = mensajeOperacion,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (mensajeError.isNotBlank()) {
                Text(
                    text = mensajeError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            if (reservas.isEmpty()) {
                Text(
                    text = "No hay reservas registradas. Presiona \"Nueva reserva\" para agregar una.",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn {
                    items(
                        items = reservas,
                        key = { reserva ->
                            reserva.id
                        }
                    ) { reserva ->
                        ReservaCard(
                            reserva = reserva,
                            onVerDetalle = {
                                reservaDetalle = reserva
                            }
                        )
                    }
                }
            }
        }

        if (mostrarDialogo) {
            ReservaDialog(
                reserva = reservaSeleccionada,
                onCerrar = {
                    mostrarDialogo = false
                },
                onGuardar = {
                        nombre,
                        cancha,
                        fecha,
                        hora,
                        estado ->

                    val seleccionada = reservaSeleccionada

                    if (seleccionada == null) {
                        viewModel.guardarReserva(
                            nombre = nombre,
                            cancha = cancha,
                            fecha = fecha,
                            hora = hora,
                            estado = estado
                        )
                    } else {
                        viewModel.actualizarReserva(
                            reserva = seleccionada,
                            nombre = nombre,
                            cancha = cancha,
                            fecha = fecha,
                            hora = hora,
                            estado = estado
                        )
                    }

                    mostrarDialogo = false
                }
            )
        }

        reservaDetalle?.let { reserva ->
            ReservaDetalleBottomSheet(
                reserva = reserva,
                onCerrar = {
                    reservaDetalle = null
                },
                onEditar = {
                    reservaDetalle = null
                    reservaSeleccionada = reserva
                    mostrarDialogo = true
                },
                onEliminar = {
                    reservaDetalle = null
                    viewModel.eliminarReserva(reserva)
                }
            )
        }
    }
}

@Composable
fun ReservaCard(
    reserva: Reserva,
    onVerDetalle: () -> Unit
) {
    Card(
        onClick = onVerDetalle,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = reserva.imagenUrl,
                contentDescription = "Imagen de ${reserva.cancha}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = reserva.nombreUsuario,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Cancha: ${reserva.cancha}"
            )

            Text(
                text = "Fecha: ${reserva.fecha}"
            )

            Text(
                text = "Hora: ${reserva.hora}"
            )

            Text(
                text = "Estado: ${reserva.estado}"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Toca para ver detalles",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaDetalleBottomSheet(
    reserva: Reserva,
    onCerrar: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onCerrar,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 32.dp
                )
        ) {
            Text(
                text = "Detalle de reserva",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            AsyncImage(
                model = reserva.imagenUrl,
                contentDescription = "Imagen de ${reserva.cancha}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = reserva.nombreUsuario,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            HorizontalDivider()

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Cancha",
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = reserva.cancha,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Fecha",
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = reserva.fecha,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Hora",
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = reserva.hora,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Estado",
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = reserva.estado,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Button(
                onClick = onEditar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Editar reserva")
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            OutlinedButton(
                onClick = onEliminar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar reserva")
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            OutlinedButton(
                onClick = onCerrar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar")
            }
        }
    }
}

@Composable
fun ReservaDialog(
    reserva: Reserva?,
    onCerrar: () -> Unit,
    onGuardar: (
        String,
        String,
        String,
        String,
        String
    ) -> Unit
) {
    var nombre by remember(reserva?.id) {
        mutableStateOf(
            reserva?.nombreUsuario.orEmpty()
        )
    }

    var cancha by remember(reserva?.id) {
        mutableStateOf(
            reserva?.cancha.orEmpty()
        )
    }

    var fecha by remember(reserva?.id) {
        mutableStateOf(
            reserva?.fecha.orEmpty()
        )
    }

    var hora by remember(reserva?.id) {
        mutableStateOf(
            reserva?.hora.orEmpty()
        )
    }

    var estado by remember(reserva?.id) {
        mutableStateOf(
            reserva?.estado ?: "Pendiente"
        )
    }

    var error by remember(reserva?.id) {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = {
            Text(
                if (reserva == null) {
                    "Nueva reserva"
                } else {
                    "Editar reserva"
                }
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        error = ""
                    },
                    label = {
                        Text("Nombre del usuario")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = cancha,
                    onValueChange = {
                        cancha = it
                        error = ""
                    },
                    label = {
                        Text("Cancha")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = fecha,
                    onValueChange = {
                        fecha = it
                        error = ""
                    },
                    label = {
                        Text("Fecha")
                    },
                    placeholder = {
                        Text("Ej: 01/06/2026")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = hora,
                    onValueChange = {
                        hora = it
                        error = ""
                    },
                    label = {
                        Text("Hora")
                    },
                    placeholder = {
                        Text("Ej: 18:00")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = estado,
                    onValueChange = {
                        estado = it
                        error = ""
                    },
                    label = {
                        Text("Estado")
                    },
                    placeholder = {
                        Text("Pendiente o Confirmada")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (error.isNotBlank()) {
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

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
                        error =
                            "Completa todos los campos antes de guardar."
                    } else {
                        onGuardar(
                            nombre,
                            cancha,
                            fecha,
                            hora,
                            estado
                        )
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onCerrar
            ) {
                Text("Cancelar")
            }
        }
    )
}
