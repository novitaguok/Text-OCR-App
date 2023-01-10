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
import com.example.ocrapp.novita.util.Constant.COLLECTION_NAME
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber


class DisplayResultFragment : Fragment(R.layout.fragment_display_result) {

    private lateinit var binding: FragmentDisplayResultBinding
    private lateinit var progressIndicator: ProgressIndicator
    private val displayResultFragmentArgs: DisplayResultFragmentArgs by navArgs()
    private val db = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDisplayResultBinding.bind(view)

        val title = bindTitleValue()
        val body = bindBodyValue()
        bindSaveButton(title, body)
    }

    private fun bindTitleValue(): String {
        var title = binding.inputTitle.editText?.text.toString()
        binding.inputTitle.editText?.doOnTextChanged { input, _, _, _ ->
            title = input.toString()
            binding.inputTitle.isErrorEnabled = false
        }
        return title
    }

    private fun bindBodyValue(): String {
        binding.inputBody.editText?.setText(displayResultFragmentArgs.text)
        var body = binding.inputBody.editText?.text.toString()
        binding.inputBody.editText?.doOnTextChanged { input, _, _, _ ->
            body = input.toString()
            binding.inputBody.isErrorEnabled = false
        }
        return body
    }

    private fun bindSaveButton(title: String, body: String) {
        binding.btnSave.setOnClickListener {
            if (title.isNotBlank() && body.isNotBlank()) {
                progressIndicator = ProgressIndicator(requireContext(), false)
                progressIndicator.show()
                val result = ResultModel(title, body)
                db.collection(COLLECTION_NAME).document()
                    .set(result)
                    .addOnSuccessListener {
                        progressIndicator.dismiss()
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.all_txt_save_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().apply {
                            popBackStack()
                            navigateUp()
                        }
                    }
                    .addOnFailureListener { e ->
                        Timber.e(getString(R.string.all_txt_error_add_document), e.cause)
                    }
            } else {
                if (title.isBlank()) {
                    binding.inputTitle.error = getString(R.string.all_txt_required)
                }
                if (body.isBlank()) {
                    binding.inputBody.error = getString(R.string.all_txt_required)
                }
            }
        }
    }
}