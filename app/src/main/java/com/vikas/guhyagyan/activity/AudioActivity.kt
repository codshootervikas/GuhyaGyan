package com.vikas.guhyagyan.activity

import PdfAdapter
import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vikas.guhyagyan.apiState.ApiState
import com.vikas.guhyagyan.databinding.ActivityAudioBinding
import com.vikas.guhyagyan.factory.AuthFactory
import com.vikas.guhyagyan.repository.AuthRepository
import com.vikas.guhyagyan.restService.RetrofitBuilder
import com.vikas.guhyagyan.utils.AudioRecordingService
import com.vikas.guhyagyan.viewmodel.AuthViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AudioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioBinding
    private var pulseAnimator: AnimatorSet? = null
    private var isRecording = false

    private val authRepository by lazy {
        AuthRepository(RetrofitBuilder.getInstance(application)!!.api)
    }

    private val authViewModel by lazy {
        ViewModelProvider(this, AuthFactory(authRepository))[AuthViewModel::class.java]
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

            val audioGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: false

            if (!audioGranted) {
                Toast.makeText(this, "Audio Permission Required", Toast.LENGTH_SHORT).show()
            }
        }

    private val recordingReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {

            val recording =
                intent?.getBooleanExtra(
                    AudioRecordingService.EXTRA_RECORDING_STATE,
                    false
                ) ?: false

            isRecording = recording

            if (recording) {
                startRecordingAnimation()
                binding.micIcon.setImageResource(android.R.drawable.ic_media_pause)
            } else {
                stopRecordingAnimation()
                binding.micIcon.setImageResource(android.R.drawable.ic_btn_speak_now)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    override fun onResume() {
        super.onResume()

        ContextCompat.registerReceiver(
            this,
            recordingReceiver,
            IntentFilter(AudioRecordingService.ACTION_UPDATE_UI),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onPause() {
        super.onPause()

        try {
            unregisterReceiver(recordingReceiver)
        } catch (_: Exception) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        pulseAnimator?.cancel()
    }

    private fun init() {

        requestPermissions()

        authViewModel.pdfList.observe(this) {

            when (it) {

                is ApiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ApiState.Success -> {

                    binding.progressBar.visibility = View.GONE

                    it.data?.data?.let { pdfList ->

                        binding.pdfList.layoutManager =
                            LinearLayoutManager(this)

                        binding.pdfList.adapter =
                            PdfAdapter(pdfList)
                    }
                }

                is ApiState.Error -> {

                    binding.progressBar.visibility = View.GONE

                    Snackbar.make(
                        binding.root,
                        it.errorMessage,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
        authViewModel.getPdfsApi()

        binding.recordBtn.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions()
                return@setOnClickListener
            }

            val intent = Intent(this, AudioRecordingService::class.java).apply {
                action = AudioRecordingService.ACTION_TOGGLE
            }

            ContextCompat.startForegroundService(this, intent)
        }

        binding.sendBtn.setOnClickListener {

            val filePath = AudioRecordingService.recordedFilePath

            if (filePath.isNullOrEmpty()) {
                Toast.makeText(this, "No recording found", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val file = File(filePath)

            if (!file.exists()) {
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val requestFile =
                file.asRequestBody("audio/m4a".toMediaTypeOrNull())

            val audioPart =
                MultipartBody.Part.createFormData(
                    "audio",
                    file.name,
                    requestFile
                )

            authViewModel.sendAudioApi(audioPart)
        }

        authViewModel.sendAudio.observe(this) {
            when (it) {
                is ApiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ApiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (it.data?.success == true) {
                        Snackbar.make(binding.root, it.data.message, Snackbar.LENGTH_SHORT).show()
                    }
                }

                is ApiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, it.errorMessage, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun requestPermissions() {

        val permissions = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.RECORD_AUDIO)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (permissions.isNotEmpty()) {
            permissionLauncher.launch(permissions.toTypedArray())
        }
    }

    private fun startRecordingAnimation() {

        val scaleX =
            ObjectAnimator.ofFloat(binding.recordBtn, View.SCALE_X, 1f, 1.15f, 1f)

        val scaleY =
            ObjectAnimator.ofFloat(binding.recordBtn, View.SCALE_Y, 1f, 1.15f, 1f)

        scaleX.repeatCount = ValueAnimator.INFINITE
        scaleY.repeatCount = ValueAnimator.INFINITE

        scaleX.duration = 800
        scaleY.duration = 800

        pulseAnimator = AnimatorSet()
        pulseAnimator?.playTogether(scaleX, scaleY)
        pulseAnimator?.start()
    }

    private fun stopRecordingAnimation() {

        pulseAnimator?.cancel()

        binding.recordBtn.scaleX = 1f
        binding.recordBtn.scaleY = 1f
    }

}