package cl.duouc.reservasport.domain

object ImagenCanchaProvider {

    fun obtener(cancha: String): String {
        return when {
            cancha.contains("tenis", ignoreCase = true) ->
                "https://picsum.photos/id/26/800/400"

            cancha.contains("futbol", ignoreCase = true) ||
                cancha.contains("fútbol", ignoreCase = true) ->
                "https://picsum.photos/id/28/800/400"

            cancha.contains("basquet", ignoreCase = true) ||
                cancha.contains("básquet", ignoreCase = true) ->
                "https://picsum.photos/id/29/800/400"

            cancha.contains("padel", ignoreCase = true) ||
                cancha.contains("pádel", ignoreCase = true) ->
                "https://picsum.photos/id/30/800/400"

            else ->
                "https://picsum.photos/id/31/800/400"
        }
    }
}
