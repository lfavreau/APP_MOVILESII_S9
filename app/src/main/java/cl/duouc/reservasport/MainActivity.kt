package cl.duouc.reservasport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import cl.duouc.reservasport.data.ReservaDatabase
import cl.duouc.reservasport.ui.ReservaScreen
import cl.duouc.reservasport.ui.ReservaViewModel
import cl.duouc.reservasport.ui.ReservaViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: ReservaViewModel by viewModels {
        ReservaViewModelFactory(
            ReservaDatabase.obtenerDatabase(applicationContext).reservaDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ReservaScreen(viewModel)
        }
    }
}