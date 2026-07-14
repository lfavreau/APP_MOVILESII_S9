package cl.duouc.reservasport.domain

object ReservaValidator {

    fun validar(
        nombre: String,
        cancha: String,
        fecha: String,
        hora: String,
        estado: String
    ) {
        require(nombre.isNotBlank()) {
            "El nombre no puede estar vacío"
        }

        require(cancha.isNotBlank()) {
            "La cancha no puede estar vacía"
        }

        require(fecha.isNotBlank()) {
            "La fecha no puede estar vacía"
        }

        require(hora.isNotBlank()) {
            "La hora no puede estar vacía"
        }

        require(estado.isNotBlank()) {
            "El estado no puede estar vacío"
        }
    }
}
