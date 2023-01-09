package com.example.ocrapp.novita.presentation.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ocrapp.novita.R
import com.example.ocrapp.novita.databinding.FragmentCameraBinding
import com.example.ocrapp.novita.presentation.component.progress_indicator.ProgressIndicator
import com.example.ocrapp.novita.util.Constant.RATIO_16_9_VALUE
import com.example.ocrapp.novita.util.Constant.RATIO_4_3_VALUE
import com.example.ocrapp.novita.util.TextAnalyser
import com.google.firebase.FirebaseApp
import com.snatik.storage.Storage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


typealias CameraTextAnalyzerListener = (text: String) -> Unit

@OptIn(DelicateCoroutinesApi::class)
class CameraFragment : Fragment(R.layout.fragment_camera) {

    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraControl: CameraControl
    private lateinit var cameraInfo: CameraInfo
    private val executor by lazy { Executors.newSingleThreadExecutor() }
    private lateinit var progressIndicator: ProgressIndicator
    lateinit var binding: FragmentCameraBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCameraBinding.bind(view)

        FirebaseApp.initializeApp(requireContext())

        progressIndicator = ProgressIndicator(requireContext(), false)
        binding.viewCamera.post {
            startCamera()
        }
        binding.btnCamera.setOnClickListener {
            progressIndicator.show()
            takePicture()
        }
        binding.btnGallery.setOnClickListener {
            findNavController().navigate(
                R.id.action_cameraFragment_to_galleryFragment,
            )
        }
    }

    @Suppress("SameParameterValue")
    private fun createFile(baseFolder: File, format: String, extension: String) =
        File(
            baseFolder,
            SimpleDateFormat(format, Locale.US).format(System.currentTimeMillis()) + extension
        )

    private fun takePicture() {
        val file = createFile(
            getOutputDirectory(
                requireContext()
            ),
            "yyyy-MM-dd-HH-mm-ss-SSS",
            ".png"
        )
        val outputFileOptions =
            ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture.takePicture(
            outputFileOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    GlobalScope.launch(Dispatchers.IO) {
                        TextAnalyser({ scanResult ->
                            if (scanResult.isEmpty()) {
                                progressIndicator.dismiss()
                                Toast.makeText(
                                    requireContext(),
                                    "No Text Detected",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                progressIndicator.dismiss()

                                findNavController().navigate(
                                    R.id.action_cameraFragment_to_displayResultFragment,
                                    Bundle().apply {
                                        putString("text", scanResult)
                                    }
                                )
                            }
                        }, requireContext(), Uri.fromFile(file)).analyseImage()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    progressIndicator.dismiss()
                    Timber.e(exception.localizedMessage!!)
                }
            })
    }

    @Suppress("DEPRECATION")
    @SuppressLint("UnsafeExperimentalUsageError")
    private fun startCamera() {
        val metrics = DisplayMetrics().also { binding.viewCamera.display.getRealMetrics(it) }
        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        val rotation = binding.viewCamera.display.rotation
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation).build()

            preview.setSurfaceProvider(binding.viewCamera.surfaceProvider)
            imageCapture = initializeImageCapture(screenAspectRatio, rotation)
            cameraProvider.unbindAll()
            try {
                val camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
                cameraControl = camera.cameraControl
                cameraInfo = camera.cameraInfo
                cameraControl.setLinearZoom(0.5f)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun getOutputDirectory(context: Context): File {
        val storage = Storage(context)
        val mediaDir = storage.internalCacheDirectory?.let {
            File(it, "OCR").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }


    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }


    private fun initializeImageCapture(
        screenAspectRatio: Int,
        rotation: Int
    ): ImageCapture {
        return ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()
    }
}