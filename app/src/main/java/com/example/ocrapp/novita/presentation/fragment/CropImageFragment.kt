package com.example.ocrapp.novita.presentation.fragment

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ocrapp.novita.R
import com.example.ocrapp.novita.databinding.FragmentCropImageBinding
import com.example.ocrapp.novita.presentation.component.progress_indicator.ProgressIndicator
import com.example.ocrapp.novita.util.CommonFunction.createFile
import com.example.ocrapp.novita.util.CommonFunction.getOutputDirectory
import com.example.ocrapp.novita.util.Constant.ARG_KEY_TEXT
import com.example.ocrapp.novita.util.Constant.IMG_FILE_DATE_FORMAT
import com.example.ocrapp.novita.util.Constant.IMG_FILE_EXT
import com.example.ocrapp.novita.util.TextAnalyser
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.util.*

class CropImageFragment : Fragment(R.layout.fragment_crop_image) {

    private var _binding: FragmentCropImageBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressIndicator: ProgressIndicator
    private val cropImageFragmentArgs: CropImageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCropImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressIndicator = ProgressIndicator(requireContext(), false)
        binding.cropImageView.setImageUriAsync(Uri.parse(cropImageFragmentArgs.uri))
        binding.btnNext.setOnClickListener {
            progressIndicator.show()
            analyzeImage()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun analyzeImage() {
        val img = binding.cropImageView.croppedImage
        val file = createFile(
            getOutputDirectory(requireContext()),
            IMG_FILE_DATE_FORMAT,
            IMG_FILE_EXT
        )

        FileOutputStream(file).use { out ->
            img.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        GlobalScope.launch(Dispatchers.IO) {
            TextAnalyser({ scanResult ->
                if (scanResult.isEmpty()) {
                    progressIndicator.dismiss()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.all_txt_no_text_detected),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    progressIndicator.dismiss()
                    findNavController().navigate(
                        R.id.action_cameraFragment_to_displayResultFragment,
                        Bundle().apply {
                            putString(ARG_KEY_TEXT, scanResult)
                        }
                    )
                }
            }, requireContext(), Uri.fromFile(file)).analyseImage()
        }
    }
}