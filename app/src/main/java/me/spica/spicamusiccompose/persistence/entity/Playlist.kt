package me.spica.spicamusiccompose.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Playlist(

    @PrimaryKey(autoGenerate = true)
    var playlistId: Long? = null,
    var playlistName: String = "自定义专辑${UUID.randomUUID()}",
    var cover: String? = null
)