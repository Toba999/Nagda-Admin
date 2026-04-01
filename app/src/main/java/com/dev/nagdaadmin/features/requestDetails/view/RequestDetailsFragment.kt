package com.dev.nagdaadmin.features.requestDetails.view

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dev.nagdaadmin.R
import com.dev.nagdaadmin.data.model.RequestModel
import com.dev.nagdaadmin.data.model.RequestStatus
import com.dev.nagdaadmin.data.model.UserModel
import com.dev.nagdaadmin.databinding.FragmentRequestDetailsBinding
import com.dev.nagdaadmin.features.requestDetails.viewModel.RequestDetailsState
import com.dev.nagdaadmin.features.requestDetails.viewModel.RequestDetailsViewModel
import com.dev.nagdaadmin.utils.DateAndTimePicker.toArabicDateTime
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RequestDetailsFragment : Fragment() {

    private var _binding: FragmentRequestDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RequestDetailsViewModel by viewModels()
    private var googleMap: GoogleMap? = null
    private var pendingLatLng: LatLng? = null

    private val requestId by lazy { arguments?.getString("requestId") ?: "" }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRequestDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init map here — not inside bindDetails
        (childFragmentManager.findFragmentById(R.id.mapPreview) as SupportMapFragment)
            .getMapAsync { map ->
                googleMap = map
                map.uiSettings.isScrollGesturesEnabled = false
                map.uiSettings.isZoomGesturesEnabled   = false
                pendingLatLng?.let { pinLocation(it) }
            }

        viewModel.getRequestDetails(requestId)
        observeState()

        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnGoHome.setOnClickListener { findNavController().popBackStack() }
        binding.btnCancel.setOnClickListener { viewModel.cancelRequest(requestId) }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.detailsState.collectLatest { state ->
                when (state) {
                    is RequestDetailsState.Loading -> showLoading(true)
                    is RequestDetailsState.Success -> {
                        showLoading(false)
                        bindDetails(state.request, state.user)
                    }
                    is RequestDetailsState.Error -> {
                        showLoading(false)
                        showSnackBar(state.message)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun bindDetails(request: RequestModel, user: UserModel) {
        with(binding) {
            statusContainer.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(root.context, request.status.colorRes)
            )
            tvLocation.text    = request.location
            tvType.text        = request.type.label
            tvDetails.text     = request.details.ifEmpty { "لا توجد تفاصيل" }
            tvDetailsTime.text = request.createdAt.toArabicDateTime()
            tvStatus.text      = request.status.label
            ivTypeIcon.setImageResource(request.type.iconRes)
            tvUserName.text       = user.fullName
            tvUserPhone.text      = user.phone
            tvUserAddress.text    = user.address
            tvUserFamilySize.text = "${user.familySize} أشخاص"
            btnCancel.isVisible   = request.status == RequestStatus.SENT
        }

        val latLng = LatLng(request.latitude, request.longitude)
        if (googleMap != null) pinLocation(latLng) else pendingLatLng = latLng
    }

    private fun pinLocation(latLng: LatLng) {
        googleMap?.apply {
            clear()
            addMarker(MarkerOptions().position(latLng))
            moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }

    private fun showLoading(show: Boolean) { binding.loadingView.root.isVisible = show }

    private fun showSnackBar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).apply {
            view.setBackgroundColor(resources.getColor(R.color.red, null))
            setTextColor(resources.getColor(R.color.white, null))
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}