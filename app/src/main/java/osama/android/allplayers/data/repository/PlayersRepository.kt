package osama.android.allplayers.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import osama.android.allplayers.api.PlayersApi
import osama.android.allplayers.data.db.AppDataBase
import osama.android.allplayers.data.remotediator.PlayersRemoteMediator
import osama.android.allplayers.models.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayersRepository @Inject constructor(
    private val playersApi: PlayersApi,
    private val db: AppDataBase
) {


    private val pagingSourceFactory = { db.playersDao.getPlayers() }

    /**
     * for caching
     */
    @ExperimentalPagingApi
    fun getPlayers(): Flow<PagingData<Player>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = PlayersRemoteMediator(
                playersApi,
                db
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    /**
     * Use this if you dont want to cache data to room
     */
//    fun getPlayers(
//    ): Flow<PagingData<Player>> {
//        return Pager(
//            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
//            pagingSourceFactory = {
//                PlayersDataSource(playersApi)
//            }
//        ).flow
//    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 15
    }

}