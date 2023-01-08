package com.example.ocrapp.novita.util

import android.content.Context
import android.net.Uri
import com.example.ocrapp.novita.presentation.fragment.CameraTextAnalyzerListener
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import timber.log.Timber

class TextAnalyser(
    private val textListener: CameraTextAnalyzerListener,
    private var context: Context,
    private val fromFile: Uri,
) {
    private val recognizer by lazy {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    fun analyseImage() {
        try {
            val img = InputImage.fromFilePath(context, fromFile)

            recognizer.process(img)
                .addOnSuccessListener { visionText ->
                    Timber.d(visionText.text)
                    textListener(visionText.text)
                }
                .addOnFailureListener { e ->
                    Timber.e(e.localizedMessage)
                }
        } catch (e: Exception) {
            Timber.e(e.localizedMessage)
        }
    }
}




