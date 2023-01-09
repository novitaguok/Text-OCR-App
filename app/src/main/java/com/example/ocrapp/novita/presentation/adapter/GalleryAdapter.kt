package com.example.ocrapp.novita.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ocrapp.novita.databinding.ItemGalleryBinding
import com.example.ocrapp.novita.presentation.model.ResultModel
import com.example.ocrapp.novita.util.DiffUtilCallback
import com.example.ocrapp.novita.util.OnCopyClickListener

class GalleryAdapter(
    var galleryList: MutableList<ResultModel>,
    private val onCopyClickListener: OnCopyClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GalleryViewHolder(
            ItemGalleryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onCopyClickListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GalleryViewHolder).bind(galleryList[position])
    }

    fun addAll(data: MutableList<ResultModel>) {
        val diffUtilCallback = DiffUtilCallback(galleryList.toMutableList(), data)
        val diffItem = DiffUtil.calculateDiff(diffUtilCallback)
        galleryList.clear()
        galleryList.addAll(data)
        diffItem.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return galleryList.size
    }
}