package cl.duouc.reservasport.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duouc.reservasport.data.session.SessionManager
import cl.duouc.reservasport.ui.InicioScreen
import cl.duouc.reservasport.ui.LoginScreen
import cl.duouc.reservasport.ui.ReservaScreen
import cl.duouc.reservasport.ui.ReservaViewModel
import cl.duouc.reservasport.ui.WeatherViewModel

object Rutas {
    const val LOGIN = "login"
    const val INICIO = "inicio"
    const val RESERVAS = "reservas"
}

@Composable
fun AppNavigation(
    reservaViewModel: ReservaViewModel,
    weatherViewModel: WeatherViewModel,
    sessionManager: SessionManager
) {
    val navController =
        rememberNavController()

    val rutaInicial =
        if (
            sessionManager.existeSesion()
        ) {
            Rutas.INICIO
        } else {
            Rutas.LOGIN
        }

    NavHost(
        navController = navController,
        startDestination = rutaInicial
    ) {
        composable(
            route = Rutas.LOGIN
        ) {
            LoginScreen(
                onLoginSuccess = { email ->
                    sessionManager.guardarSesion(
                        email
                    )

                    navegarSinRetorno(
                        navController =
                            navController,
                        destino =
                            Rutas.INICIO
                    )
                }
            )
        }

        composable(
            route = Rutas.INICIO
        ) {
            InicioScreen(
                emailUsuario =
                    sessionManager.obtenerEmail(),

                weatherUiState =
                    weatherViewModel.uiState,

                onActualizarClima = {
                    weatherViewModel.cargarClima()
                },

                onSimularAlertaLluvia = {
                    weatherViewModel
                        .simularAlertaLluvia()
                },

                onVerReservas = {
                    navController.navigate(
                        Rutas.RESERVAS
                    )
                },

                onCerrarSesion = {
                    sessionManager.cerrarSesion()

                    navegarSinRetorno(
                        navController =
                            navController,
                        destino =
                            Rutas.LOGIN
                    )
                }
            )
        }

        composable(
            route = Rutas.RESERVAS
        ) {
            ReservaScreen(
                viewModel =
                    reservaViewModel,

                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}

private fun navegarSinRetorno(
    navController: NavHostController,
    destino: String
) {
    navController.navigate(
        destino
    ) {
        popUpTo(
            navController.graph.startDestinationId
        ) {
            inclusive = true
        }

        launchSingleTop = true
    }
}