package com.example.voice_recorder.playback

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}