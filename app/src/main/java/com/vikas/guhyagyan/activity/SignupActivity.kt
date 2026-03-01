package com.vikas.guhyagyan.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.vikas.guhyagyan.R
import com.vikas.guhyagyan.apiState.ApiState
import com.vikas.guhyagyan.databinding.ActivitySignupBinding
import com.vikas.guhyagyan.factory.AuthFactory
import com.vikas.guhyagyan.models.file.FileRemoveRequest
import com.vikas.guhyagyan.models.register.RegisterRequest
import com.vikas.guhyagyan.repository.AuthRepository
import com.vikas.guhyagyan.restService.RetrofitBuilder
import com.vikas.guhyagyan.utils.UriToFile
import com.vikas.guhyagyan.viewmodel.AuthViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class SignupActivity : AppCompatActivity(R.layout.activity_signup) {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var cameraUri: Uri
    private var imageId: String? = null
    private var imageUri: Uri? = null

    private val authRepository by lazy {
        AuthRepository(RetrofitBuilder.getInstance(application)!!.api)
    }

    private val authViewModel by lazy {
        ViewModelProvider(this, AuthFactory(authRepository))[AuthViewModel::class.java]
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            try {
                val imageFile = UriToFile(this).getImageBody(cameraUri)
                val requestFile = imageFile?.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imagePart = requestFile?.let { it1 ->
                    MultipartBody.Part.createFormData(
                        "file", imageFile.name, it1
                    )
                }
                if (imagePart != null) {
                    imageUri = cameraUri
                    authViewModel.fileSaveApi(imagePart)
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Snackbar.make(binding.root, e.message.toString(), Snackbar.LENGTH_SHORT).show()
            }

        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

            // User cancelled picker
            if (uri == null) {
                binding.progressBar.visibility = View.GONE
                return@registerForActivityResult
            }

            binding.progressBar.visibility = View.VISIBLE

            try {
                val mimeType = contentResolver.getType(uri).orEmpty()


                val file = UriToFile(this).getImageBody(uri)
                if (file == null) {
                    Snackbar.make(
                        binding.root,
                        "Unable to get file",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    binding.progressBar.visibility = View.GONE
                    return@registerForActivityResult
                }

                val requestBody =
                    file.asRequestBody(mimeType.toMediaTypeOrNull())

                val filePart = MultipartBody.Part.createFormData(
                    name = "file",
                    filename = file.name,
                    body = requestBody
                )

                // Loader should be hidden after API response
                imageUri = uri
                authViewModel.fileSaveApi(filePart)

            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Snackbar.make(
                    binding.root,
                    e.message ?: "Something went wrong",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cameraUri = openCamera()!!
        init()
    }

    private fun init() {

        authViewModel.register.observe(this) {
            when (it) {
                is ApiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ApiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT).show()
                    it.data?.data?.verifyKey?.let { key ->
                        startActivity(
                            Intent(this, ValidateOtpActivity::class.java)
                                .putExtra("key", key)
                                .putExtra("email", binding.etEmail.text.toString().trim())
                        )
                    }
                }

                is ApiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

            }
        }

        binding.registerBtn.setOnClickListener {
            if (validateFields()) {
                authViewModel.registerApi(
                    RegisterRequest(
                        email = binding.etEmail.text.toString().trim(),
                        phone = binding.etPhone.text.toString().trim(),
                        name = binding.etName.text.toString().trim(),
                        password = binding.etPassword1.text.toString().trim(),
                        image = imageId
                    )
                )
            }
        }

        binding.profileImg.setOnClickListener {
            if (imageId == null)
                dialog()
        }

        authViewModel.fileSave.observe(this) { response ->
            when (response) {
                is ApiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                }

                is ApiState.Success -> {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    binding.progressBar.visibility = View.GONE
                    imageId = response.data?.data?.imageId
                    binding.profileImg.setImageURI(imageUri)
                    if (response.data?.success == true) {
                        binding.deleteBtn.visibility = View.VISIBLE
                    }
                }

                is ApiState.Error -> {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, response.errorMessage, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        authViewModel.fileRemove.observe(this) { response ->
            when (response) {
                is ApiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                }

                is ApiState.Success -> {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    binding.progressBar.visibility = View.GONE
                    if (response.data?.success == true) {
                        imageId = null
                        imageUri = null
                        binding.deleteBtn.visibility = View.GONE
                        binding.profileImg.setImageResource(R.drawable.person)
                    }
                }

                is ApiState.Error -> {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, response.errorMessage, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        binding.deleteBtn.setOnClickListener {
            authViewModel.fileRemoveApi(FileRemoveRequest(imageId))
        }

    }

    private fun validateFields(): Boolean {
        val name = binding.etName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val pass1 = binding.etPassword1.text.toString().trim()
        val pass2 = binding.etPassword2.text.toString().trim()

        var isValid = true

        // Name Validation
        if (name.isEmpty()) {
            binding.nameLayout.error = "Name is required"
            isValid = false
        } else {
            binding.nameLayout.error = null
        }

        // Phone Validation (10 digits)
        if (phone.isEmpty()) {
            binding.phoneLayout.error = "Phone is required"
            isValid = false
        } else if (!phone.matches(Regex("^[6-9][0-9]{9}$"))) {
            binding.phoneLayout.error = "Enter valid 10-digit phone"
            isValid = false
        } else {
            binding.phoneLayout.error = null
        }

        // Email Validation (Optional but must be valid if entered)
        if (email.isEmpty()) {
            binding.emailLayout.error = "Email is required"
            isValid = false
        } else if (email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) {
            binding.emailLayout.error = "Enter valid email"
            isValid = false
        } else {
            binding.emailLayout.error = null
        }


        if (pass1.isEmpty()) {
            binding.passwordLayout1.error = "password is required"
            isValid = false
        } else {
            binding.passwordLayout1.error = null
        }

        if (pass2.isEmpty()) {
            binding.passwordLayout2.error = "confirm password is required"
            isValid = false
        } else {
            binding.passwordLayout2.error = null
        }

        if (pass1 != pass2) {
            binding.passwordLayout2.error = "Password does not match"
            isValid = false
        } else {
            binding.passwordLayout2.error = null
        }

        return isValid
    }

    private fun openCamera(): Uri? {
        val image = File(this.application?.filesDir, "${System.currentTimeMillis()}.png")
        return FileProvider.getUriForFile(
            this, "com.vikas.guhyagyan.fileProvider", image
        )
    }

    private fun dialog() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Photo")
        builder.setItems(items) { dialog, item ->
            when {
                items[item] == "Take Photo" -> {
                    binding.progressBar.visibility = View.VISIBLE
                    cameraLauncher.launch(cameraUri)
                }

                items[item] == "Choose from Gallery" -> {
                    binding.progressBar.visibility = View.VISIBLE
                    galleryLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }

                items[item] == "Cancel" -> {
                    dialog.dismiss()
                }

            }
        }
        builder.show()
    }

}