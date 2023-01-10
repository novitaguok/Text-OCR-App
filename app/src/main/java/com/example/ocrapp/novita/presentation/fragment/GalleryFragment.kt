package com.example.ocrapp.novita.presentation.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ocrapp.novita.R
import com.example.ocrapp.novita.databinding.FragmentGalleryBinding
import com.example.ocrapp.novita.presentation.adapter.GalleryAdapter
import com.example.ocrapp.novita.presentation.model.ResultModel
import com.example.ocrapp.novita.util.Constant.COLLECTION_NAME
import com.example.ocrapp.novita.util.Constant.DOC_BODY_FIELD_KEY
import com.example.ocrapp.novita.util.Constant.DOC_TITLE_FIELD_KEY
import com.example.ocrapp.novita.util.OnCopyClickListener
import com.google.firebase.firestore.FirebaseFirestore

class GalleryFragment : Fragment(R.layout.fragment_gallery), OnCopyClickListener {

    private lateinit var binding: FragmentGalleryBinding
    private var galleryAdapter: GalleryAdapter? = null
    private val db = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentGalleryBinding.bind(view)

        initRecyclerView()
        initData()
    }

    private fun initRecyclerView() {
        galleryAdapter = GalleryAdapter(arrayListOf(), this)
        binding.lstGallery.layoutManager = LinearLayoutManager(context)
        binding.lstGallery.adapter = galleryAdapter
    }

    private fun initData() {
        val galleryList = mutableListOf<ResultModel>()
        db.collection(COLLECTION_NAME).get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (document in it.result) {
                    val title = document.data[DOC_TITLE_FIELD_KEY].toString()
                    val body = document.data[DOC_BODY_FIELD_KEY].toString()
                    val result = ResultModel(title = title, body = body, id = document.id)
                    galleryList.add(result)
                }
            }
            galleryAdapter?.addAll(galleryList)
        }.addOnFailureListener {
            it.printStackTrace()
        }
    }

    override fun onCopyClicked(text: String) {
        val clipboard = getSystemService(requireContext(), ClipboardManager::class.java)
        val clip = ClipData.newPlainText("label", text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(
            requireContext(),
            getString(R.string.all_txt_copy_to_clipboard),
            Toast.LENGTH_SHORT
        ).show()
    }
}