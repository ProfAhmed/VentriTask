package osama.android.allplayers.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "players_table")
data class Player(
    val name: String,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val full_name: String,
    val description: String?,
) : Parcelable