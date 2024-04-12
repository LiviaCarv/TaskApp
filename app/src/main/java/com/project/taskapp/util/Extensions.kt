package com.project.taskapp.util

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.taskapp.R
import com.project.taskapp.databinding.BottomSheetDialogBinding

fun Fragment.initToolBar(toolbar: Toolbar) {
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    (activity as AppCompatActivity).title = ""
    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    toolbar.setNavigationOnClickListener { activity?.onBackPressedDispatcher?.onBackPressed() }

}

fun Fragment.showBottomSheet(
    titleDialog: Int? = null,
    titleButton: Int? = null,
    message: Int,
    onClick: () -> Unit = {}
) {
    val bottomSheetDialog = BottomSheetDialog(requireContext(), )
    val binding: BottomSheetDialogBinding =
        BottomSheetDialogBinding.inflate(layoutInflater, null, false)
    binding.txtBtSheetTitle.text = getText(titleDialog ?: R.string.warning)
    binding.btnOk.text = getText(titleButton ?: R.string.ok)
    binding.txtWarningMessage.text = getText(message)

    binding.btnOk.setOnClickListener {
        onClick()
        bottomSheetDialog.dismiss()
    }
    bottomSheetDialog.setContentView(binding.root)
    bottomSheetDialog.show()

}