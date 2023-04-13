package me.spica.spicamusiccompose.service.notification

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.session.MediaButtonReceiver
import me.spica.spicamusiccompose.service.MusicService
import me.spica.spicamusiccompose.BuildConfig
import me.spica.spicamusiccompose.R
import me.spica.spicamusiccompose.persistence.entity.Song
import me.spica.spicamusiccompose.playback.PlaybackStateManager
import me.spica.spicamusiccompose.player.Queue

class MediaSessionComponent(
    private val context: Context,
    private val listener: Listener
) :
    MediaSessionCompat.Callback(),
    PlaybackStateManager.Listener {


    private val mediaSession =
        MediaSessionCompat(context, context.packageName).apply {
            isActive = true
            setQueueTitle(context.getString(R.string.queue))
        }

    private val playbackManager = PlaybackStateManager.getInstance()

    private val notification = NotificationComponent(context, mediaSession.sessionToken)


    init {
        playbackManager.addListener(this)
        mediaSession.setCallback(this)
    }

    fun handleMediaButtonIntent(intent: Intent) {
        MediaButtonReceiver.handleIntent(mediaSession, intent)
    }

    interface Listener {
        fun onPostNotification(notification: NotificationComponent)
    }

    fun release() {
        playbackManager.removeListener(this)
        mediaSession.apply {
            isActive = false
            release()
        }
    }

    override fun onIndexMoved(queue: Queue) {
        updateMediaMetadata(queue.currentSong())
    }

    override fun onNewListLoad(queue: Queue) {
        super.onNewListLoad(queue)
        updateMediaMetadata(queue.currentSong())
    }

    override fun onStateChanged(isPlaying: Boolean) {
        super.onStateChanged(isPlaying)
        notification.updatePlaying(playbackManager.playerState.isPlaying)
        listener.onPostNotification(notification)
    }

    override fun onPlay() {
        playbackManager.setPlaying(true)
        notification.updatePlaying(true)
        listener.onPostNotification(notification)
    }

    override fun onPause() {
        playbackManager.setPlaying(false)
        notification.updatePlaying(false)
        listener.onPostNotification(notification)
    }


    override fun onSkipToNext() {
        playbackManager.playNext()
    }

    override fun onSkipToPrevious() {
        playbackManager.playPre()
    }

    override fun onSeekTo(position: Long) {
        playbackManager.seekTo(position)
    }

    override fun onCustomAction(action: String?, extras: Bundle?) {
        super.onCustomAction(action, extras)
        context.sendBroadcast(Intent(action))
    }


    override fun onStop() {
        context.sendBroadcast(Intent(MusicService.ACTION_EXIT))
    }


    private fun updateMediaMetadata(song: Song?) {
        if (song == null) {
            mediaSession.setMetadata(emptyMetadata)
            return
        }
        val title = song.displayName
        val artist = song.artist
        val builder =
            MediaMetadataCompat.Builder()
                .putText(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putText(MediaMetadataCompat.METADATA_KEY_ALBUM, song.displayName)
                .putText(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .putText(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, song.artist)
                .putText(MediaMetadataCompat.METADATA_KEY_AUTHOR, artist)
                .putText(MediaMetadataCompat.METADATA_KEY_COMPOSER, artist)
                .putText(MediaMetadataCompat.METADATA_KEY_WRITER, artist)
                .putText(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, title)
                .putText(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, artist)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, song.duration)

        builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ART, BitmapFactory.decodeResource(context.resources, R.mipmap.default_cover))

        val metadata = builder.build()
        mediaSession.setMetadata(metadata)
        notification.updateMetadata(metadata)
        listener.onPostNotification(notification)
    }


    companion object {
        const val METADATA_KEY_PARENT = BuildConfig.APPLICATION_ID + ".metadata.PARENT"
        private val emptyMetadata = MediaMetadataCompat.Builder().build()
        private const val ACTIONS =
            PlaybackStateCompat.ACTION_PLAY or
                PlaybackStateCompat.ACTION_PAUSE or
                PlaybackStateCompat.ACTION_PLAY_PAUSE or
                PlaybackStateCompat.ACTION_SET_REPEAT_MODE or
                PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE or
                PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM or
                PlaybackStateCompat.ACTION_SEEK_TO or
                PlaybackStateCompat.ACTION_STOP
    }
}