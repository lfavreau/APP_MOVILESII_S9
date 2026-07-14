package cl.duouc.reservasport.data.repository

import cl.duouc.reservasport.data.Reserva
import cl.duouc.reservasport.data.ReservaDao
import kotlinx.coroutines.flow.Flow

class ReservaRepository(
    private val dao: ReservaDao
) {
    fun obtenerReservas(): Flow<List<Reserva>> {
        return dao.obtenerReservas()
    }

    suspend fun insertar(reserva: Reserva) {
        dao.insertarReserva(reserva)
    }

    suspend fun actualizar(reserva: Reserva) {
        dao.actualizarReserva(reserva)
    }

    suspend fun eliminar(reserva: Reserva) {
        dao.eliminarReserva(reserva)
    }
}
