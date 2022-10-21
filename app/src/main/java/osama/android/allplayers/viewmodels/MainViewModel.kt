package osama.android.allplayers.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import osama.android.allplayers.data.repository.PlayersRepository
import osama.android.allplayers.models.Player
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PlayersRepository
) : ViewModel() {
    private var currentResult: Flow<PagingData<Player>>? = null

    @ExperimentalPagingApi
    fun searchPlayers(): Flow<PagingData<Player>> {
        val newResult: Flow<PagingData<Player>> =
            repository.getPlayers().cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }

}