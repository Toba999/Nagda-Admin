package com.dev.nagdaadmin.features.requests.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dev.nagdaadmin.R
import com.dev.nagdaadmin.data.model.RequestStatus
import com.dev.nagdaadmin.databinding.ItemFilterTabBinding

class FilterTabAdapter(
    private val onFilterSelected: (RequestStatus?) -> Unit
) : RecyclerView.Adapter<FilterTabAdapter.ViewHolder>() {

    private var selectedPosition = 0

    data class FilterTab(val label: String, val status: RequestStatus?)

    private val tabs = listOf(
        FilterTab("الكل",         null),
        FilterTab("جديد",         RequestStatus.SENT),
        FilterTab("تم الاستلام",  RequestStatus.RECEIVED),
        FilterTab("جاري التعامل", RequestStatus.IN_PROGRESS),
        FilterTab("في الطريق",   RequestStatus.ON_THE_WAY),
        FilterTab("تم التعامل",  RequestStatus.DONE),
        FilterTab("تم الالغاء",  RequestStatus.CANCELLED)
    )

    class ViewHolder(val binding: ItemFilterTabBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemFilterTabBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val tab = tabs[holder.getAdapterPosition() ]
        val isSelected = holder.getAdapterPosition()  == selectedPosition

        with(holder.binding.tvFilter) {
            text = tab.label
            background = ContextCompat.getDrawable(
                context,
                if (isSelected) R.drawable.bg_filter_active else R.drawable.bg_filter_inactive
            )
            setTextColor(ContextCompat.getColor(
                context,
                if (isSelected) R.color.white else R.color.primaryText
            ))
        }

        holder.binding.tvFilter.setOnClickListener {
            val prev = selectedPosition
            selectedPosition = holder.getAdapterPosition()
            notifyItemChanged(prev)
            notifyItemChanged(holder.getAdapterPosition() )
            onFilterSelected(tab.status)
        }
    }

    override fun getItemCount() = tabs.size
}