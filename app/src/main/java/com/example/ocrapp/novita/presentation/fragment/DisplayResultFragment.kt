package com.example.ocrapp.novita.presentation.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ocrapp.novita.R
import com.example.ocrapp.novita.databinding.FragmentDisplayResultBinding
import com.example.ocrapp.novita.presentation.component.progress_indicator.ProgressIndicator
import com.example.ocrapp.novita.presentation.model.ResultModel
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber


class DisplayResultFragment : Fragment(R.layout.fragment_display_result) {

    private lateinit var binding: FragmentDisplayResultBinding
    private lateinit var progressIndicator: ProgressIndicator
    private val displayResultFragmentArgs: DisplayResultFragmentArgs by navArgs()
    val db = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDisplayResultBinding.bind(view)
        binding.txtResult.append(displayResultFragmentArgs.text)

        var title = binding.inputTitle.editText?.text.toString()
        binding.inputTitle.editText?.doOnTextChanged { input, _, _, _ ->
            title = input.toString()
            binding.inputTitle.isErrorEnabled = false
        }

        binding.btnSave.setOnClickListener {
            if (title.isBlank()) {
                binding.inputTitle.error = "Required"
            } else {
                progressIndicator = ProgressIndicator(requireContext(), false)
                progressIndicator.show()
                val result =
                    hashMapOf("result" to ResultModel(title, displayResultFragmentArgs.toString()))
                db.collection("ocr")
                    .add(result)
                    .addOnSuccessListener { documentReference ->
                        progressIndicator.dismiss()
                        Toast.makeText(
                            requireContext(),
                            "Saved successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigateUp()
                    }
                    .addOnFailureListener { e ->
                        Timber.w("Error adding document", e)
                    }
            }
        }
    }
}