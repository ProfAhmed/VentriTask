package osama.android.allplayers.data.remotediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import osama.android.allplayers.api.PlayersApi
import osama.android.allplayers.data.db.AppDataBase
import osama.android.allplayers.data.entity.RemoteKeys
import osama.android.allplayers.models.Player
import osama.android.allplayers.utils.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException


@ExperimentalPagingApi
class PlayersRemoteMediator(
    private val service: PlayersApi,
    private val db: AppDataBase
) : RemoteMediator<Int, Player>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Player>): MediatorResult {
        val key = when (loadType) {
            LoadType.REFRESH -> {
                if (db.playersDao.count() > 0) return MediatorResult.Success(false)
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                getKey()
            }
        }

        try {
            if (key != null) {
                if (key.isEndReached) return MediatorResult.Success(endOfPaginationReached = true)
            }

            val page: Int = key?.nextKey ?: STARTING_PAGE_INDEX
            val apiResponse = service.getPlayers(state.config.pageSize, page)

            db.withTransaction {
                val nextKey = page + 1

                db.remoteKeysDao.insertKey(
                    RemoteKeys(
                        0,
                        nextKey = nextKey,
                        isEndReached = false
                    )
                )
                db.playersDao.insertMultiplePlayers(apiResponse)
            }
            return MediatorResult.Success(endOfPaginationReached = false)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKey(): RemoteKeys? {
        return db.remoteKeysDao.getKeys().firstOrNull()
    }


}