package com.example.ocrapp.novita.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.ocrapp.novita.databinding.ItemGalleryBinding
import com.example.ocrapp.novita.presentation.model.ResultModel
import com.example.ocrapp.novita.util.OnCopyClickListener

class GalleryViewHolder(
    private val binding: ItemGalleryBinding,
    private val onCopyClickListener: OnCopyClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: ResultModel) {
        binding.apply {
            txtTitle.text = data.title
            txtBody.text = data.body
            btnCopy.setOnClickListener {
                onCopyClickListener.onCopyClicked(data.body)
            }
        }
    }
}