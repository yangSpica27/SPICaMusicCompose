package me.spica.spicamusiccompose.audio.scanner

import android.content.Context
import android.provider.MediaStore
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import me.spica.spicamusiccompose.persistence.entity.Song
import me.spica.spicamusiccompose.utils.contentResolverSafe

/**
 * 简单、快速的 Android 媒体存储数据库读取方式
 */
class MediaStoreMediaProvider(private val context: Context) : MediaProvider {


    override val type: String
        get() = "MediaStore"

    override fun findSongs(): Flow<FlowEvent<List<Song>, MessageProgress>> =
        flow {
            val songs = arrayListOf<Song>()

            val songCursor = context.contentResolverSafe.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                localAudioColumns,
                "${MediaStore.Audio.Media.IS_MUSIC}=1 OR ${MediaStore.Audio.Media.IS_PODCAST}=1",
                null, null
            )


            songCursor?.use {
                val size = songCursor.count
                var progress = 0
                while (currentCoroutineContext().isActive && songCursor.moveToNext()) {
                    val song = Song(
                        mediaStoreId = songCursor.getLong(songCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)),
                        path = songCursor.getStringOrNull(songCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)) ?: "",
                        size = songCursor.getLongOrNull(songCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.SIZE)) ?: 0,
                        displayName = songCursor.getStringOrNull(songCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE))
                            ?: songCursor.getStringOrNull(songCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISPLAY_NAME)) ?: MediaStore.UNKNOWN_STRING,
                        artist = songCursor.getStringOrNull(songCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)) ?: "",
                        like = false,
                        sort = 0,
                        playTimes = -1,
                        lastPlayTime = -1,
                        duration = songCursor.getLongOrNull(songCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)) ?: 0,
                    )
                    songs.add(song)
                    progress++
                    emit(
                        FlowEvent.Progress(
                            MessageProgress(
                                message = listOf(
                                    song.artist,
                                    song.displayName
                                ).joinToString(" • "),
                                progress = Progress(progress, size)
                            )
                        )
                    )
                }
            }
            emit(FlowEvent.Success(songs))
        }


    // 查询字段
    private val localAudioColumns = arrayOf(
        MediaStore.Audio.AudioColumns._ID, // 音频id
        MediaStore.Audio.AudioColumns.DATA, // 音频路径
        MediaStore.Audio.AudioColumns.SIZE, // 音频字节大小
        MediaStore.Audio.AudioColumns.DISPLAY_NAME, // 音频名称 xxx.amr
        MediaStore.Audio.AudioColumns.TITLE, // 音频标题
        MediaStore.Audio.AudioColumns.DATE_ADDED, // 音频添加到MediaProvider的时间
        MediaStore.Audio.AudioColumns.DATE_MODIFIED, // 上次修改时间，该列用于内部MediaScanner扫描，外部不要修改
        MediaStore.Audio.AudioColumns.MIME_TYPE, // 音频类型 audio/3gp
        MediaStore.Audio.AudioColumns.DURATION, // 音频时长
        MediaStore.Audio.AudioColumns.BOOKMARK, // 上次音频的回放位置
        MediaStore.Audio.AudioColumns.ARTIST_ID, // 艺人id
        MediaStore.Audio.AudioColumns.ARTIST, // 艺人名称
        MediaStore.Audio.AudioColumns.ALBUM_ID, // 艺人专辑id
        MediaStore.Audio.AudioColumns.ALBUM, // 艺人专辑名称
        MediaStore.Audio.AudioColumns.TRACK, MediaStore.Audio.AudioColumns.YEAR, // 录制音频的年份
        MediaStore.Audio.AudioColumns.IS_MUSIC, // 是否为音乐音频
        MediaStore.Audio.AudioColumns.IS_PODCAST, MediaStore.Audio.AudioColumns.IS_RINGTONE, // 是否为警告音频
        MediaStore.Audio.AudioColumns.IS_ALARM, // 是否为闹钟音频
        MediaStore.Audio.AudioColumns.IS_NOTIFICATION // 是否为通知音频
    )

}