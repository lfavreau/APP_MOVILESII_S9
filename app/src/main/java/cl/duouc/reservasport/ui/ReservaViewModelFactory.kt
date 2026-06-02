package cl.duouc.reservasport.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cl.duouc.reservasport.data.ReservaDao

class ReservaViewModelFactory(
    private val dao: ReservaDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReservaViewModel(dao) as T
    }
}