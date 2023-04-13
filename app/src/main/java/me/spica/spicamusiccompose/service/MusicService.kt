package me.spica.spicamusiccompose.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import me.spica.spicamusiccompose.BuildConfig
import me.spica.spicamusiccompose.persistence.entity.Song
import me.spica.spicamusiccompose.playback.PlaybackStateManager
import me.spica.spicamusiccompose.player.IPlayer
import me.spica.spicamusiccompose.service.notification.MediaSessionComponent
import me.spica.spicamusiccompose.service.notification.NotificationComponent

private const val MY_MEDIA_ROOT_ID = "media_root_id"
private const val MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id"

class MusicService : MediaBrowserServiceCompat(), Player.Listener, IPlayer, MediaSessionComponent.Listener {

    private var mediaSession: MediaSessionCompat? = null


    private val fftAudioProcessor = PlaybackStateManager.getInstance().fftAudioProcessor

    private lateinit var foregroundManager: ForegroundManager

    private var hasPlayed = false

    private lateinit var exoPlayer: ExoPlayer

    private lateinit var mediaSessionComponent: MediaSessionComponent

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSessionCompat(this, "spica_music")

        // 避免降采样 采取最近似的采样
        val extractorsFactory = DefaultMediaSourceFactory(
            this,
            DefaultExtractorsFactory().setConstantBitrateSeekingEnabled(true)
        )

        exoPlayer = ExoPlayer.Builder(this, object : DefaultRenderersFactory(this) {
            override fun buildAudioRenderers(
                context: Context,
                extensionRendererMode: Int,
                mediaCodecSelector: MediaCodecSelector,
                enableDecoderFallback: Boolean,
                audioSink: AudioSink,
                eventHandler: Handler,
                eventListener: AudioRendererEventListener,
                out: ArrayList<Renderer>
            ) {

                out.add(
                    MediaCodecAudioRenderer(
                        context,
                        mediaCodecSelector,
                        enableDecoderFallback,
                        eventHandler,
                        eventListener,
                        DefaultAudioSink.Builder()
                            .setAudioCapabilities(AudioCapabilities.getCapabilities(context))
                            .setAudioProcessors(
                                arrayOf(
                                    fftAudioProcessor,
                                    PlaybackStateManager.getInstance().equalizerAudioProcessor,
                                    PlaybackStateManager.getInstance().replayGainAudioProcessor
                                )
                            )
                            .build()
                    )
                )
                super.buildAudioRenderers(
                    context,
                    extensionRendererMode,
                    mediaCodecSelector,
                    enableDecoderFallback,
                    audioSink,
                    eventHandler,
                    eventListener, out
                )
            }
        })
            .setWakeMode(C.WAKE_MODE_LOCAL)
            .setMediaSourceFactory(extractorsFactory)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                true
            )
            .build().also {
                it.addListener(this)
            }

        PlaybackStateManager.getInstance().registerPlayer(this)
        foregroundManager = ForegroundManager(this)
        mediaSessionComponent = MediaSessionComponent(this, this)
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action == Intent.ACTION_MEDIA_BUTTON) {
            mediaSessionComponent.handleMediaButtonIntent(intent)
        }
        return START_NOT_STICKY
    }

    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)
        if (events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED) && player.playWhenReady) {
            // 开始播放了
            hasPlayed = true
        }


        if (events.containsAny(
                Player.EVENT_PLAY_WHEN_READY_CHANGED,
                Player.EVENT_IS_PLAYING_CHANGED,
                Player.EVENT_POSITION_DISCONTINUITY
            )
        ) {
            // 控制器同步状态
            PlaybackStateManager.getInstance().synchronizeState()
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        // 播放错误 直接下一首
        PlaybackStateManager.getInstance().playNext()
    }

    /**
     * 连接时候触发
     */
    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {

        return if (
            false
        ) {
            // 允许连接
            BrowserRoot(MY_MEDIA_ROOT_ID, null)
        } else {
            // 不允许连接
            BrowserRoot(MY_EMPTY_MEDIA_ROOT_ID, null)
        }
    }


    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        if (playbackState == Player.STATE_ENDED) {
            // 根据播放模式处理播放结束后的操作
            PlaybackStateManager.getInstance().playNext()
        }
    }

    /**
     * 客户端连接后，可以通过重复调用 MediaBrowserCompat.subscribe() 来遍历内容层次结构，
     */
    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        //  对于不允许连接的 客户端返回空
        if (MY_EMPTY_MEDIA_ROOT_ID == parentId) {
            result.sendResult(null)
            return
        }
        // 其他情况返回真正的播放列表
        val mediaItems = mutableListOf<MediaBrowserCompat.MediaItem>()
        result.sendResult(mediaItems)
    }


    override fun onDestroy() {
        super.onDestroy()
        // 解除绑定
        PlaybackStateManager.getInstance().unRegisterPlayer()
        exoPlayer.release()
        foregroundManager.release()
        mediaSessionComponent.release()
    }

    override fun loadSong(song: Song?, play: Boolean) {
        if (song == null) {
            exoPlayer.stop()
            return
        }
        exoPlayer.setMediaItem(MediaItem.fromUri(song.getSongUri()))
        exoPlayer.prepare()
        exoPlayer.playWhenReady = play
    }

    override fun getState(durationMs: Long): IPlayer.State {
        return IPlayer.State(
            exoPlayer.isPlaying,
            exoPlayer.currentPosition.coerceAtLeast(0)
                .coerceAtMost(durationMs)
        )
    }


    override fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
    }

    override fun setPlaying(isPlaying: Boolean) {
        exoPlayer.playWhenReady = isPlaying
    }

    // 广播通知
    companion object {
        const val ACTION_INC_REPEAT_MODE = BuildConfig.APPLICATION_ID + ".action.LOOP"
        const val ACTION_INVERT_SHUFFLE = BuildConfig.APPLICATION_ID + ".action.SHUFFLE"
        const val ACTION_SKIP_PREV = BuildConfig.APPLICATION_ID + ".action.PREV"
        const val ACTION_PLAY_PAUSE = BuildConfig.APPLICATION_ID + ".action.PLAY_PAUSE"
        const val ACTION_SKIP_NEXT = BuildConfig.APPLICATION_ID + ".action.NEXT"
        const val ACTION_EXIT = BuildConfig.APPLICATION_ID + ".action.EXIT"
        private const val REWIND_THRESHOLD = 3000L
    }


    override fun onPostNotification(notification: NotificationComponent) {
        if (hasPlayed) {
            if (!foregroundManager.tryStartForeground(notification)) {
                notification.post()
            }
        }
    }
}