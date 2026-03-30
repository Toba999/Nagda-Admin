package com.dev.nagdaadmin.features.register.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dev.nagdaadmin.R
import com.dev.nagdaadmin.databinding.FragmentRegisterBinding
import com.dev.nagdaadmin.features.register.models.RegisterState
import com.dev.nagdaadmin.features.register.viewModel.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.registerBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.registerButton.setOnClickListener {
            val fullName = binding.etName.text.toString().trim()
            val phone    = binding.etPhone.text.toString().trim()
            val mail     = binding.etMail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val role     = binding.etJob.text.toString().trim()

            if (validateInputs(fullName, phone, mail,role, password)) {
                viewModel.registerAdmin(
                    fullName = fullName,
                    phone    = phone,
                    email    = mail,
                    role     = role,
                    password = password
                )
            }
        }
    }

    private fun validateInputs(
        fullName: String,
        phone: String,
        mail: String,
        role: String,
        password: String
    ): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return when {
            fullName.isEmpty()        -> { showSnackBar("الاسم الكامل مطلوب", true); false }
            phone.isEmpty()           -> { showSnackBar("رقم الهاتف مطلوب", true); false }
            phone.length < 10         -> { showSnackBar("رقم الهاتف غير صحيح", true); false }
            mail.isEmpty()            -> { showSnackBar("البريد الإلكتروني مطلوب", true); false }
            !mail.matches(emailRegex) -> { showSnackBar("البريد الإلكتروني غير صحيح", true); false }
            password.isEmpty()        -> { showSnackBar("كلمة المرور مطلوبة", true); false }
            password.length < 6       -> { showSnackBar("كلمة المرور يجب أن تكون 6 أحرف على الأقل", true); false }
            role.isEmpty()            -> { showSnackBar("الوظيفة مطلوبة", true); false }
            else -> true
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.registerState.collectLatest { state ->
                when (state) {
                    is RegisterState.Loading -> showLoading(true)
                    is RegisterState.Success -> navigateToLogin()
                    is RegisterState.Error   -> showSnackBar(state.message, true)
                    is RegisterState.Idle    -> Unit
                }
            }
        }
    }

    private fun navigateToLogin() {
        showLoading(false)
        viewModel.resetState()
        showSnackBar("تم إنشاء حساب المشرف بنجاح", false)
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.RegisterFragment, true)
            .setLaunchSingleTop(true)
            .build()
        findNavController().navigate(R.id.LoginFragment, null, navOptions)
    }

    private fun showSnackBar(message: String, isError: Boolean) {
        showLoading(false)
        val snackBar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(
            resources.getColor(if (isError) R.color.red else R.color.green, null)
        )
        snackBar.setTextColor(resources.getColor(R.color.white, null))
        snackBar.show()
    }

    private fun showLoading(isShown: Boolean) {
        binding.loadingView.root.isVisible = isShown
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}