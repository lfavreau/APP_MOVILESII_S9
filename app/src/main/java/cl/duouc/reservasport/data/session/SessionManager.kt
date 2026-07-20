package cl.duouc.reservasport.data.session

import android.content.Context

class SessionManager(
    context: Context
) {
    companion object {
        private const val PREFERENCES_NAME =
            "reservasport_session"

        private const val KEY_LOGGED_IN =
            "logged_in"

        private const val KEY_USER_EMAIL =
            "user_email"
    }

    private val preferences =
        context.getSharedPreferences(
            PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )

    fun guardarSesion(
        email: String
    ) {
        preferences
            .edit()
            .putBoolean(
                KEY_LOGGED_IN,
                true
            )
            .putString(
                KEY_USER_EMAIL,
                email
            )
            .apply()
    }

    fun existeSesion(): Boolean {
        return preferences.getBoolean(
            KEY_LOGGED_IN,
            false
        )
    }

    fun obtenerEmail(): String {
        return preferences.getString(
            KEY_USER_EMAIL,
            ""
        ).orEmpty()
    }

    fun cerrarSesion() {
        preferences
            .edit()
            .clear()
            .apply()
    }
}