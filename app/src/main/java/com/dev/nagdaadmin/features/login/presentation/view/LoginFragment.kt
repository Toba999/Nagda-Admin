package com.dev.nagdaadmin.features.login.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dev.nagdaadmin.R
import com.dev.nagdaadmin.databinding.FragmentLoginBinding
import com.dev.nagdaadmin.features.login.domain.model.LoginState
import com.dev.nagdaadmin.features.login.presentation.viewModel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkForBiometricLogin()
        observeLoginState()
        binding.fingerprintCard.setOnClickListener { showBiometricPrompt() }
        binding.registerBtn.setOnClickListener {
            findNavController().navigate(R.id.RegisterFragment)
        }

        binding.loginButton.setOnClickListener {
            val phone    = binding.phoneEt.text.toString().trim()
            val password = binding.passwordEt.text.toString().trim()
            if (validateInputs(phone, password)) {
                viewModel.login(phone, password)
            }
        }
    }

    private fun observeLoginState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collectLatest { state ->
                when (state) {
                    is LoginState.Loading -> showLoading(true)
                    is LoginState.Success -> navigateToHome()
                    is LoginState.Error   -> showSnackBar(state.message, true)
                    else -> Unit
                }
            }
        }
    }

    private fun checkForBiometricLogin() {
        if (!viewModel.isBiometricEnabled()) return
        val biometricManager = BiometricManager.from(requireContext())
        val canAuthenticate = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        ) == BiometricManager.BIOMETRIC_SUCCESS

        if (canAuthenticate) {
            binding.fingerprintCard.isVisible = true
            binding.fingerPrintText.isVisible = true
            showBiometricPrompt()
        } else {
            showSnackBar("يرجي تفعيل تسجيل الدخول بالبصمة في جهازك", true)
        }
    }

    private fun showBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(requireContext())
        val biometricPrompt = BiometricPrompt(
            this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    viewModel.loginWithBiometric()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showSnackBar("فشل التحقق من بصمة الإصبع", true)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    showSnackBar("خطأ: $errString", true)
                }
            })

        BiometricPrompt.PromptInfo.Builder()
            .setTitle("تسجيل الدخول ببصمة الإصبع")
            .setSubtitle("استخدم بصمة إصبعك لتسجيل الدخول")
            .setNegativeButtonText("إلغاء")
            .build()
            .also { biometricPrompt.authenticate(it) }
    }

    private fun validateInputs(phone: String, password: String): Boolean {
        return when {
            phone.isEmpty()    -> { showSnackBar("رقم الهاتف مطلوب", true); false }
            password.isEmpty() -> { showSnackBar("كلمة المرور مطلوبة", true); false }
            else -> true
        }
    }

    private fun navigateToHome() {
        showLoading(false)
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.LoginFragment, true)
            .setLaunchSingleTop(true)
            .build()
        findNavController().navigate(R.id.containerFragment, null, navOptions)
    }

    private fun showSnackBar(message: String, isError: Boolean) {
        showLoading(false)
        val snackBar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
        if (isError) {
            snackBar.view.setBackgroundColor(resources.getColor(R.color.red, null))
            snackBar.setTextColor(resources.getColor(R.color.white, null))
        }
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