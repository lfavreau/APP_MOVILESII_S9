package cl.duouc.reservasport.ui

import cl.duouc.reservasport.data.Reserva
import cl.duouc.reservasport.data.ReservaDao
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReservaViewModel(private val dao: ReservaDao) : ViewModel() {

    val reservas = dao.obtenerReservas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun guardarReserva(
        nombre: String,
        cancha: String,
        fecha: String,
        hora: String,
        estado: String
    ) {
        viewModelScope.launch {
            dao.insertarReserva(
                Reserva(
                    nombreUsuario = nombre,
                    cancha = cancha,
                    fecha = fecha,
                    hora = hora,
                    estado = estado
                )
            )
        }
    }

    fun actualizarReserva(
        reserva: Reserva,
        nombre: String,
        cancha: String,
        fecha: String,
        hora: String,
        estado: String
    ) {
        viewModelScope.launch {
            dao.actualizarReserva(
                reserva.copy(
                    nombreUsuario = nombre,
                    cancha = cancha,
                    fecha = fecha,
                    hora = hora,
                    estado = estado
                )
            )
        }
    }

    fun eliminarReserva(reserva: Reserva) {
        viewModelScope.launch {
            dao.eliminarReserva(reserva)
        }
    }
}