package com.dev.nagdaadmin.features.dialogs

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import com.dev.nagdaadmin.databinding.FragmentSuccessDialogBinding

class SuccessDialogFragment : DialogFragment() {

    private var _binding: FragmentSuccessDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuccessDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOk.setOnClickListener {
            dismiss()

        }

        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            val margin = 100
            val displayMetrics = context.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            setLayout(screenWidth - (margin * 2), ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            val layoutParams = attributes
            layoutParams.dimAmount = 0.5f
            attributes = layoutParams
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
    }


}
