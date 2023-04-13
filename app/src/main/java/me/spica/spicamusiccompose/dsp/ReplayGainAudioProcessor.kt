package me.spica.spicamusiccompose.dsp

import androidx.core.math.MathUtils
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.audio.AudioProcessor
import com.google.android.exoplayer2.audio.BaseAudioProcessor
import me.spica.spicamusiccompose.dsp.ByteUtils.Int24_MAX_VALUE
import me.spica.spicamusiccompose.dsp.ByteUtils.Int24_MIN_VALUE
import me.spica.spicamusiccompose.dsp.ByteUtils.getInt24
import me.spica.spicamusiccompose.dsp.ByteUtils.putInt24
import java.nio.ByteBuffer


class ReplayGainAudioProcessor(var preAmpGain: Double = 0.0) : BaseAudioProcessor() {


    // 歌曲的增益
    var trackGain: Double? = null


    private val gain: Double
        get() = preAmpGain + (trackGain ?: 0.0)

    override fun onConfigure(inputAudioFormat: AudioProcessor.AudioFormat): AudioProcessor.AudioFormat {
        if (inputAudioFormat.encoding != C.ENCODING_PCM_16BIT &&
            inputAudioFormat.encoding != C.ENCODING_PCM_24BIT
        ) {
            throw AudioProcessor.UnhandledAudioFormatException(inputAudioFormat)
        }
        return inputAudioFormat
    }

    override fun queueInput(inputBuffer: ByteBuffer) {


        if (gain != 0.0) {
            val size = inputBuffer.remaining()
            val buffer = replaceOutputBuffer(size)
            val delta = gain.fromDb()
            when (outputAudioFormat.encoding) {
                C.ENCODING_PCM_16BIT -> {
                    while (inputBuffer.hasRemaining()) {
                        val sample = inputBuffer.short
                        val targetSample = MathUtils.clamp((sample * delta), Short.MIN_VALUE.toDouble(), Short.MAX_VALUE.toDouble()).toInt().toShort()
                        buffer.putShort(targetSample)
                    }
                }
                C.ENCODING_PCM_24BIT -> {
                    while (inputBuffer.hasRemaining()) {
                        val sample = inputBuffer.getInt24()
                        val targetSample = MathUtils.clamp(sample * delta, Int24_MIN_VALUE.toDouble(), Int24_MAX_VALUE.toDouble()).toInt()
                        buffer.putInt24(targetSample)
                    }
                }
                else -> {
                    // No op
                }
            }
            inputBuffer.position(inputBuffer.limit())
            buffer.flip()
        } else {
            val remaining = inputBuffer.remaining()
            if (remaining == 0) {
                return
            }
            replaceOutputBuffer(remaining).put(inputBuffer).flip()
        }
    }

    companion object {
        const val maxPreAmpGain = 12
    }
}