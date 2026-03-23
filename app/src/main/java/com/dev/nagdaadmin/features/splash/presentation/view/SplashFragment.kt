package com.dev.nagdaadmin.features.splash.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dev.nagdaadmin.R
import com.dev.nagdaadmin.databinding.FragmentSplashBinding
import com.dev.nagdaadmin.utils.LocationPermissionUtil
import com.dev.nagdaadmin.utils.SharedPrefManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private var isDialogShowing = false
    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        checkLocationPermissions()
    }

    private fun checkLocationPermissions() {
        when {
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ->
                LocationPermissionUtil.showRationaleDialog(this)

            else ->
                LocationPermissionUtil.requestPermission(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LocationPermissionUtil.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkGPSAndNavigate()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),Manifest.permission.ACCESS_FINE_LOCATION)) {
                LocationPermissionUtil.showPermissionDeniedDialog(this)
            }
        }
    }

    private fun checkGPSAndNavigate() {
        if (!LocationPermissionUtil.isGPSEnabled(requireContext())) {
            if (isDialogShowing) return
            isDialogShowing = true
            LocationPermissionUtil.showEnableGPSDialog(this) { isDialogShowing = false }
        } else {
            navigateNext()
        }
    }

    private fun navigateNext() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.SplashFragment, true)
            .setLaunchSingleTop(true)
            .build()

//        val destination = if (hasSeenOnboarding())
//            R.id.LoginFragment
//        else
//            R.id.OnboardingFragment

        viewLifecycleOwner.lifecycleScope.launch {
            delay(3000)
            findNavController().navigate( R.id.LoginFragment, null, navOptions)
        }
    }

    private fun hasSeenOnboarding() =
        sharedPrefManager.getBoolean("onboarding_done", false)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}