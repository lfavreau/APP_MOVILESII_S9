package cl.duouc.reservasport.ui

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit
) {
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var mensajeError by remember {
        mutableStateOf("")
    }

    var mostrarRecuperacion by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ReservaSport",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                mensajeError = ""
            },
            label = {
                Text("Correo electrónico")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                mensajeError = ""
            },
            label = {
                Text("Contraseña")
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (mensajeError.isNotBlank()) {
            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = {
                val emailLimpio = email.trim()
                val passwordLimpia = password.trim()

                when {
                    emailLimpio.isBlank() -> {
                        mensajeError =
                            "Ingresa tu correo electrónico."
                    }

                    !Patterns.EMAIL_ADDRESS
                        .matcher(emailLimpio)
                        .matches() -> {
                        mensajeError =
                            "El correo electrónico no es válido."
                    }

                    passwordLimpia.isBlank() -> {
                        mensajeError =
                            "Ingresa tu contraseña."
                    }

                    passwordLimpia.length < 6 -> {
                        mensajeError =
                            "La contraseña debe tener al menos 6 caracteres."
                    }

                    !emailLimpio.equals(
                        "demo@reservasport.cl",
                        ignoreCase = true
                    ) ||
                            passwordLimpia != "Reserva123" -> {
                        mensajeError =
                            "Correo o contraseña incorrectos."
                    }

                    else -> {
                        onLoginSuccess(
                            emailLimpio.lowercase()
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        TextButton(
            onClick = {
                mostrarRecuperacion = true
            }
        ) {
            Text("¿Olvidaste tu contraseña?")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Usuario de prueba: demo@reservasport.cl",
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = "Contraseña: Reserva123",
            style = MaterialTheme.typography.bodySmall
        )
    }

    if (mostrarRecuperacion) {
        RecuperarPasswordDialog(
            onCerrar = {
                mostrarRecuperacion = false
            }
        )
    }
}

@Composable
private fun RecuperarPasswordDialog(
    onCerrar: () -> Unit
) {
    var email by remember {
        mutableStateOf("")
    }

    var mensaje by remember {
        mutableStateOf("")
    }

    var esError by remember {
        mutableStateOf(false)
    }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = {
            Text("Recuperar contraseña")
        },
        text = {
            Column {
                Text(
                    "Ingresa tu correo para solicitar la recuperación."
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        mensaje = ""
                        esError = false
                    },
                    label = {
                        Text("Correo electrónico")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (mensaje.isNotBlank()) {
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = mensaje,
                        color = if (esError) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val emailLimpio = email.trim()

                    when {
                        emailLimpio.isBlank() -> {
                            mensaje =
                                "Ingresa tu correo electrónico."
                            esError = true
                        }

                        !Patterns.EMAIL_ADDRESS
                            .matcher(emailLimpio)
                            .matches() -> {
                            mensaje =
                                "El correo electrónico no es válido."
                            esError = true
                        }

                        !emailLimpio.equals(
                            "demo@reservasport.cl",
                            ignoreCase = true
                        ) -> {
                            mensaje =
                                "No existe una cuenta asociada a este correo."
                            esError = true
                        }

                        else -> {
                            mensaje =
                                "Solicitud de recuperación registrada para demo@reservasport.cl."
                            esError = false
                        }
                    }
                }
            ) {
                Text("Recuperar")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onCerrar
            ) {
                Text("Cerrar")
            }
        }
    )
}