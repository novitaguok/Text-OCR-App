package com.example.ocrapp.novita.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.ocrapp.novita.R
import com.example.ocrapp.novita.databinding.FragmentDisplayResultBinding

class DisplayResultFragment : Fragment(R.layout.fragment_display_result) {

    lateinit var binding: FragmentDisplayResultBinding
    private val displayResultFragmentArgs: DisplayResultFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDisplayResultBinding.bind(view)
        binding.txtResult.append(displayResultFragmentArgs.text)
    }
}