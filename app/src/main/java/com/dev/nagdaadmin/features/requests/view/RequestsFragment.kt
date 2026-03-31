package com.dev.nagdaadmin.features.requests.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.nagdaadmin.R
import com.dev.nagdaadmin.databinding.FragmentRequestsBinding
import com.dev.nagdaadmin.features.requests.viewModel.RequestsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RequestsFragment : Fragment() {

    private var _binding: FragmentRequestsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RequestsViewModel by viewModels()
    private lateinit var adapter: RequestsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeState()
        viewModel.getUserRequests()
    }

    private fun setupRecyclerView() {
        adapter = RequestsAdapter(emptyList()) { id ->
            val bundle = Bundle().apply { putString("requestId", id) }
            findNavController().navigate(R.id.requestDetailsFragment, bundle)
        }
        binding.rvRequests.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@RequestsFragment.adapter
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.requestsState.collectLatest { state ->
                when (state) {
                    is RequestsState.Loading -> {
                        showLoading(true)
                        showEmpty(false)
                        showList(false)
                    }
                    is RequestsState.Success -> {
                        showLoading(false)
                        showEmpty(false)
                        showList(true)
                        adapter.updateList(state.requests)
                    }
                    is RequestsState.Empty -> {
                        showLoading(false)
                        showEmpty(true)
                        showList(false)
                    }
                    is RequestsState.Error -> {
                        showLoading(false)
                        showEmpty(true)
                        //showSnackBar(state.message)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showLoading(show: Boolean) { binding.loadingView.root.isVisible = show }
    private fun showEmpty(show: Boolean)   { binding.tvEmpty.isVisible = show }
    private fun showList(show: Boolean)    { binding.rvRequests.isVisible = show }

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