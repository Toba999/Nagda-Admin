package com.dev.nagdaadmin.features.dialogs

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import com.dev.nagdaadmin.R
import com.dev.nagdaadmin.databinding.FragmentLogoutDialogBinding
import com.dev.nagdaadmin.utils.SharedPrefManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LogoutDialogFragment : DialogFragment() {

    private var _binding: FragmentLogoutDialogBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogoutDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnOk.setOnClickListener {
                sharedPrefManager.clearAll()
                dismiss()
                navigateToLogin()
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
        }

    }

    private fun navigateToLogin() {
        val navController = requireActivity().findNavController(R.id.nav_host_fragment)
        navController.navigate(R.id.LoginFragment)
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            val margin = 40
            val displayMetrics = context.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            setLayout(screenWidth - (margin * 2), ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            val layoutParams = attributes
            layoutParams.dimAmount = 0.6f
            attributes = layoutParams
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
    }


}
