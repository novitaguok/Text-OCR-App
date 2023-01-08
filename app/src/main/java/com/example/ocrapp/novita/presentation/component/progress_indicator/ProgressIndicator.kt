package com.example.ocrapp.novita.presentation.component.progress_indicator

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.ocrapp.novita.databinding.ProgressIndicatorBinding

class ProgressIndicator(ctx: Context, private val isCancelable: Boolean = true) : AlertDialog(ctx) {

    override fun show() {
        super.show()
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = ProgressIndicatorBinding.inflate(inflater)
        setContentView(view.root)
    }

    override fun onStart() {
        super.onStart()
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.setCancelable(isCancelable)
    }

    override fun setCancelable(flag: Boolean) {
        super.setCancelable(isCancelable)
    }
}