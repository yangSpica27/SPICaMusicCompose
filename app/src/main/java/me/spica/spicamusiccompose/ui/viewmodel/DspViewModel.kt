package me.spica.spicamusiccompose.ui.viewmodel

import androidx.lifecycle.ViewModel
import me.spica.spicamusiccompose.dsp.Equalizer
import me.spica.spicamusiccompose.dsp.EqualizerBand
import me.spica.spicamusiccompose.playback.PlaybackStateManager


class DspViewModel() : ViewModel() {
    // 设置EQ预设
    fun setEqPreset(preset: Equalizer.Presets.Preset) {
        PlaybackStateManager.getInstance().equalizerAudioProcessor.preset = preset
    }

    // 设置均衡器
    fun updateBand(band: EqualizerBand) {
        PlaybackStateManager.getInstance().equalizerAudioProcessor.preset.bands.forEach { currentPresetBand ->
            Equalizer.Presets.custom.bands.first { it.centerFrequency == currentPresetBand.centerFrequency }
                .apply {
                    gain = currentPresetBand.gain
                    if (currentPresetBand.centerFrequency == band.centerFrequency) {
                        gain = band.gain
                    }
                }
        }
    }


    // 设置回放增益
    fun setPreAmpGain(gain: Double) {
        PlaybackStateManager.getInstance().replayGainAudioProcessor.preAmpGain = gain

    }
}