package com.example.ocrapp.novita.util

import androidx.recyclerview.widget.DiffUtil
import com.example.ocrapp.novita.presentation.model.ResultModel
import javax.annotation.Nullable

class DiffUtilCallback(
    private val oldList: MutableList<ResultModel>,
    private val newList: MutableList<ResultModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldList[oldPosition] == newList[newPosition]
    }

    @Nullable
    override fun getChangePayload(oldCourse: Int, newPosition: Int): Any? {
        return super.getChangePayload(oldCourse, newPosition)
    }
}
