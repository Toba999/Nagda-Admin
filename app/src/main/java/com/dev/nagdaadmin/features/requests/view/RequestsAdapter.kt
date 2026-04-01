package com.dev.nagdaadmin.features.requests.view

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dev.nagdaadmin.data.model.RequestModel
import com.dev.nagdaadmin.databinding.ItemRequestBinding
import com.dev.nagdaadmin.utils.DateAndTimePicker.toArabicTimeAgo

class RequestsAdapter(
    private var items: List<RequestModel>,
    private val onMoreClick: (String) -> Unit
) : RecyclerView.Adapter<RequestsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRequestBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemRequestBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            statusContainer.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(root.context, item.status.colorRes)
            )
            tvStatus.text = item.status.label
            tvLocation.text = item.location
            tvType.text = item.type.label
            ivTypeIcon.setImageResource(item.type.iconRes)
            tvDetails.text = item.details
            tvMore.setOnClickListener { onMoreClick(item.id) }
            tvDetailsTime.text = item.createdAt.toArabicTimeAgo()
        }
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<RequestModel>) {
        items = newItems
        notifyDataSetChanged()
    }
}