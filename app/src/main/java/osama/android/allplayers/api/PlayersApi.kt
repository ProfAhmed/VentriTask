package osama.android.allplayers.api

import osama.android.allplayers.models.Player
import retrofit2.http.GET
import retrofit2.http.Query

interface PlayersApi {

    @GET("users/JakeWharton/repos")
    suspend fun getPlayers(
        @Query("per_page") per_page: Int?,
        @Query("page") page: Int?,
    ): List<Player>

}