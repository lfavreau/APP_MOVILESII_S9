package cl.duouc.reservasport

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.pressBack
import androidx.test.platform.app.InstrumentationRegistry
import cl.duouc.reservasport.data.ReservaDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ReservaScreenTest {

    private val preparacionRule =
        object : TestRule {

            override fun apply(
                base: Statement,
                description: Description
            ): Statement {
                return object : Statement() {

                    override fun evaluate() {
                        prepararEstadoInicial(
                            nombrePrueba =
                                description.methodName
                        )

                        base.evaluate()
                    }
                }
            }
        }

    private val composeRule =
        createAndroidComposeRule<MainActivity>()

    @get:Rule
    val reglas: RuleChain =
        RuleChain
            .outerRule(preparacionRule)
            .around(composeRule)

    @Test
    fun login_muestraFormulario() {
        esperarNodo(
            texto = "Correo electrónico",
            timeoutMillis = 15_000
        )

        composeRule
            .onNodeWithText("Correo electrónico")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Contraseña")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Ingresar")
            .assertIsDisplayed()
    }

    @Test
    fun loginCorrecto_muestraInicio() {
        esperarNodo(
            texto = "Correo electrónico",
            timeoutMillis = 15_000
        )

        composeRule
            .onNodeWithText("Correo electrónico")
            .assertIsDisplayed()
            .performTextReplacement(
                "demo@reservasport.cl"
            )

        composeRule
            .onNodeWithText("Contraseña")
            .assertIsDisplayed()
            .performTextReplacement(
                "Reserva123"
            )

        composeRule
            .onNodeWithText("Ingresar")
            .assertIsDisplayed()
            .performClick()

        esperarNodo(
            texto = "Gestión de reservas deportivas",
            timeoutMillis = 20_000
        )

        composeRule
            .onNodeWithText(
                "Gestión de reservas deportivas"
            )
            .assertIsDisplayed()

        composeRule
            .onNodeWithText(
                "demo@reservasport.cl"
            )
            .assertIsDisplayed()
    }

    @Test
    fun clima_muestraSeccionEnInicio() {
        esperarPantallaInicio()

        composeRule
            .onNodeWithText(
                "Clima en Concepción"
            )
            .assertIsDisplayed()
    }

    @Test
    fun clima_muestraEstadoInicial() {
        esperarPantallaInicio()

        composeRule
            .onNodeWithText(
                "Clima en Concepción"
            )
            .assertIsDisplayed()

        esperarResultadoClimatico()

        composeRule
            .onNodeWithText(
                "Clima en Concepción"
            )
            .assertIsDisplayed()
    }

    @Test
    fun actualizarClima_mantieneSeccionVisible() {
        esperarPantallaInicio()
        esperarResultadoClimatico()

        if (nodoExiste("Actualizar clima")) {
            composeRule
                .onNodeWithText("Actualizar clima")
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()
        } else {
            composeRule
                .onNodeWithText("Reintentar")
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()
        }

        esperarNodo(
            texto = "Clima en Concepción",
            timeoutMillis = 15_000
        )

        composeRule
            .onNodeWithText(
                "Clima en Concepción"
            )
            .assertIsDisplayed()
    }

    @Test
    fun botonAlertaLluvia_estaDisponible() {
        esperarPantallaInicio()
        esperarResultadoClimatico()

        composeRule
            .onNodeWithText(
                "Probar alerta de lluvia"
            )
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun botonAlertaLluvia_puedePresionarse() {
        esperarPantallaInicio()
        esperarResultadoClimatico()

        composeRule
            .onNodeWithText(
                "Probar alerta de lluvia"
            )
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        composeRule.waitForIdle()

        composeRule
            .onNodeWithText(
                "Clima en Concepción"
            )
            .assertIsDisplayed()
    }

    @Test
    fun cerrarSesion_regresaAlLogin() {
        esperarPantallaInicio()

        composeRule
            .onNodeWithText("Cerrar sesión")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        esperarNodo(
            texto = "Correo electrónico",
            timeoutMillis = 15_000
        )

        composeRule
            .onNodeWithText(
                "Correo electrónico"
            )
            .assertIsDisplayed()
    }

    @Test
    fun navegarAReservas_muestraPantallaReservas() {
        esperarPantallaInicio()
        abrirReservas()

        composeRule
            .onNodeWithText(
                "Reservas deportivas"
            )
            .assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription(
                "Agregar reserva"
            )
            .assertIsDisplayed()
    }

    @Test
    fun nuevaReserva_abreFormulario() {
        esperarPantallaInicio()
        abrirReservas()
        abrirFormularioNuevaReserva()

        composeRule
            .onNodeWithText(
                "Nueva reserva"
            )
            .assertIsDisplayed()

        composeRule
            .onNodeWithText(
                "Nombre del usuario"
            )
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Cancha")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Fecha")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Hora")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Estado")
            .assertIsDisplayed()
    }

    @Test
    fun formularioVacio_muestraMensajeValidacion() {
        esperarPantallaInicio()
        abrirReservas()
        abrirFormularioNuevaReserva()

        composeRule
            .onNodeWithText("Guardar")
            .assertIsDisplayed()
            .performClick()

        composeRule
            .onNodeWithText(
                "Completa todos los campos antes de guardar."
            )
            .assertIsDisplayed()
    }

    @Test
    fun registrarReserva_muestraReservaEnLista() {
        esperarPantallaInicio()
        abrirReservas()

        val nombre =
            "Prueba Automática"

        crearReserva(
            nombre = nombre,
            cancha = "Tenis",
            fecha = "30/09/2026",
            hora = "18:00",
            estado = "Confirmada"
        )

        esperarNodo(
            texto = nombre,
            timeoutMillis = 15_000
        )

        composeRule
            .onNodeWithText(nombre)
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun tocarReserva_abreBottomSheet() {
        esperarPantallaInicio()
        abrirReservas()

        val nombre =
            "Reserva Bottom Sheet"

        crearReserva(
            nombre = nombre,
            cancha = "Fútbol",
            fecha = "30/09/2026",
            hora = "20:00",
            estado = "Confirmada"
        )

        esperarNodo(
            texto = nombre,
            timeoutMillis = 15_000
        )

        composeRule
            .onAllNodesWithText(nombre)[0]
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        esperarNodo(
            texto = "Detalle de reserva",
            timeoutMillis = 10_000
        )

        composeRule
            .onNodeWithText(
                "Detalle de reserva"
            )
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(nombre)[1]
            .assertIsDisplayed()

        composeRule
            .onNodeWithText(
                "Editar reserva"
            )
            .assertIsDisplayed()

        composeRule
            .onNodeWithText(
                "Eliminar reserva"
            )
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Cerrar")
            .assertIsDisplayed()
    }

    @Test
    fun cerrarBottomSheet_ocultaDetalle() {
        esperarPantallaInicio()
        abrirReservas()

        val nombre =
            "Reserva Cierre Sheet"

        crearReserva(
            nombre = nombre,
            cancha = "Tenis",
            fecha = "01/10/2026",
            hora = "17:00",
            estado = "Pendiente"
        )

        esperarNodo(
            texto = nombre,
            timeoutMillis = 15_000
        )

        composeRule
            .onNodeWithText(nombre)
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        esperarNodo(
            texto = "Detalle de reserva",
            timeoutMillis = 10_000
        )

        composeRule
            .onNodeWithText(
                "Detalle de reserva"
            )
            .assertIsDisplayed()

        pressBack()

        esperarNodo(
            texto = "Reservas deportivas",
            timeoutMillis = 10_000
        )

        composeRule
            .onNodeWithText(
                "Reservas deportivas"
            )
            .assertIsDisplayed()
    }

    private fun esperarPantallaInicio() {
        esperarNodo(
            texto = "Gestión de reservas deportivas",
            timeoutMillis = 20_000
        )

        composeRule
            .onNodeWithText(
                "Gestión de reservas deportivas"
            )
            .assertIsDisplayed()
    }

    private fun abrirReservas() {
        composeRule
            .onNodeWithText("Ver reservas")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        esperarNodo(
            texto = "Reservas deportivas",
            timeoutMillis = 10_000
        )

        composeRule
            .onNodeWithText(
                "Reservas deportivas"
            )
            .assertIsDisplayed()
    }

    private fun abrirFormularioNuevaReserva() {
        composeRule
            .onNodeWithContentDescription(
                "Agregar reserva"
            )
            .assertIsDisplayed()
            .performClick()

        esperarNodo(
            texto = "Nueva reserva",
            timeoutMillis = 10_000
        )
    }

    private fun esperarResultadoClimatico() {
        composeRule.waitUntil(
            timeoutMillis = 30_000
        ) {
            nodoExiste("Actualizar clima") ||
                    nodoExiste("Reintentar")
        }

        esperarNodo(
            texto = "Probar alerta de lluvia",
            timeoutMillis = 10_000
        )
    }

    private fun esperarNodo(
        texto: String,
        timeoutMillis: Long
    ) {
        composeRule.waitUntil(
            timeoutMillis = timeoutMillis
        ) {
            nodoExiste(texto)
        }
    }

    private fun nodoExiste(
        texto: String
    ): Boolean {
        return composeRule
            .onAllNodesWithText(texto)
            .fetchSemanticsNodes(
                atLeastOneRootRequired = false
            )
            .isNotEmpty()
    }

    private fun crearReserva(
        nombre: String,
        cancha: String,
        fecha: String,
        hora: String,
        estado: String
    ) {
        abrirFormularioNuevaReserva()

        composeRule
            .onNodeWithText(
                "Nombre del usuario"
            )
            .assertIsDisplayed()
            .performTextReplacement(nombre)

        composeRule
            .onNodeWithText("Cancha")
            .assertIsDisplayed()
            .performTextReplacement(cancha)

        composeRule
            .onNodeWithText("Fecha")
            .assertIsDisplayed()
            .performTextReplacement(fecha)

        composeRule
            .onNodeWithText("Hora")
            .assertIsDisplayed()
            .performTextReplacement(hora)

        composeRule
            .onNodeWithText("Estado")
            .assertIsDisplayed()
            .performTextClearance()

        composeRule
            .onNodeWithText("Estado")
            .assertIsDisplayed()
            .performTextReplacement(estado)

        composeRule
            .onNodeWithText("Guardar")
            .assertIsDisplayed()
            .performClick()
    }

    private fun prepararEstadoInicial(
        nombrePrueba: String
    ) {
        concederPermisoNotificaciones()

        val contexto =
            InstrumentationRegistry
                .getInstrumentation()
                .targetContext

        limpiarBaseDeDatos(contexto)

        val preferences =
            contexto.getSharedPreferences(
                "reservasport_session",
                Context.MODE_PRIVATE
            )

        preferences
            .edit()
            .clear()
            .commit()

        val pruebaRequiereLogin =
            nombrePrueba == "login_muestraFormulario" ||
                    nombrePrueba ==
                    "loginCorrecto_muestraInicio"

        if (!pruebaRequiereLogin) {
            preferences
                .edit()
                .putBoolean(
                    "logged_in",
                    true
                )
                .putString(
                    "user_email",
                    "demo@reservasport.cl"
                )
                .commit()
        }
    }

    private fun limpiarBaseDeDatos(
        contexto: Context
    ) {
        runCatching {
            ReservaDatabase
                .obtenerDatabase(contexto)
                .clearAllTables()
        }
    }

    private fun concederPermisoNotificaciones() {
        if (
            Build.VERSION.SDK_INT <
            Build.VERSION_CODES.TIRAMISU
        ) {
            return
        }

        val instrumentation =
            InstrumentationRegistry
                .getInstrumentation()

        val packageName =
            instrumentation
                .targetContext
                .packageName

        runCatching {
            instrumentation
                .uiAutomation
                .executeShellCommand(
                    "pm grant $packageName " +
                            Manifest.permission.POST_NOTIFICATIONS
                )
                .close()
        }
    }
}