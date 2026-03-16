package com.vikas.guhyagyan.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.vikas.guhyagyan.activity.AudioActivity

class AudioRecordingService : Service() {

    private var recorder: MediaRecorder? = null
    private var isRecording = false
    private var recordingStartTime = 0L

    companion object {

        const val ACTION_TOGGLE = "TOGGLE_RECORDING"
        const val ACTION_UPDATE_UI = "UPDATE_UI"
        const val EXTRA_RECORDING_STATE = "RECORDING_STATE"
        const val CHANNEL_ID = "record_channel"

        // Activity can access this to upload audio
        var recordedFilePath: String? = null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent?.action == ACTION_TOGGLE) {
            toggleRecording()
        }

        return START_STICKY
    }

    /* ---------------- TOGGLE ---------------- */

    private fun toggleRecording() {

        if (isRecording) {
            stopRecording()
        } else {
            startRecording()
        }
    }

    /* ---------------- START RECORDING ---------------- */

    private fun startRecording() {

        try {

            recordedFilePath =
                "${externalCacheDir?.absolutePath}/audio_${System.currentTimeMillis()}.m4a"

            recorder = MediaRecorder().apply {

                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(recordedFilePath)

                prepare()
                start()
            }

            recordingStartTime = System.currentTimeMillis()
            isRecording = true

            startForeground(1, createNotification())
            sendStateToActivity()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /* ---------------- STOP RECORDING ---------------- */

    private fun stopRecording() {

        try {

            // prevent crash if recording < 1 second
            if (System.currentTimeMillis() - recordingStartTime < 800) {
                return
            }

            recorder?.apply {

                try {
                    stop()
                } catch (e: RuntimeException) {
                    e.printStackTrace()
                }

                release()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        recorder = null
        isRecording = false

        updateNotification()
        sendStateToActivity()
    }

    /* ---------------- SEND STATE TO ACTIVITY ---------------- */

    private fun sendStateToActivity() {

        val intent = Intent(ACTION_UPDATE_UI)
        intent.putExtra(EXTRA_RECORDING_STATE, isRecording)
        sendBroadcast(intent)
    }

    /* ---------------- NOTIFICATION ---------------- */

    private fun createNotification(): Notification {

        createNotificationChannel()

        val toggleIntent = Intent(this, AudioRecordingService::class.java).apply {
            action = ACTION_TOGGLE
        }

        val togglePendingIntent = PendingIntent.getService(
            this,
            0,
            toggleIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val activityIntent = Intent(this, AudioActivity::class.java)

        val activityPendingIntent = PendingIntent.getActivity(
            this,
            1,
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val buttonText = if (isRecording) "Pause" else "Play"
        val icon =
            if (isRecording) android.R.drawable.ic_media_pause
            else android.R.drawable.ic_media_play

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Audio Recorder")
            .setContentText(
                if (isRecording) "Recording in progress"
                else "Recording paused"
            )
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .setContentIntent(activityPendingIntent)
            .setOngoing(true)
            .addAction(icon, buttonText, togglePendingIntent)
            .build()
    }

    /* ---------------- UPDATE NOTIFICATION ---------------- */

    private fun updateNotification() {

        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(1, createNotification())
    }

    /* ---------------- CHANNEL ---------------- */

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Audio Recording",
                NotificationManager.IMPORTANCE_LOW
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}