package com.siju.networksample.ui.view

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.siju.networksample.R
import com.siju.networksample.databinding.ItemHeaderBinding
import com.siju.networksample.databinding.ItemRowBinding
import com.siju.networksample.ui.model.UiItem

sealed class ListItem {
    data class Header(val listId: Int, var isCollapsed: Boolean, val itemCount: Int) : ListItem()
    data class Item(val uiItem: UiItem) : ListItem()
}

class ItemsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // We'll store the data in a structure of groups and flatten as needed.
    private val groups = mutableListOf<GroupData>()

    // Flattened list that includes headers and items depending on collapse state.
    private val displayList = mutableListOf<ListItem>()

    fun submitData(map: Map<Int, List<UiItem>>) {
        groups.clear()
        // Create group data with default isCollapsed = false (expanded)
        map.toSortedMap().forEach { (listId, items) ->
            android.util.Log.d("SS", "$listId items size:${items.size}")
            groups.add(GroupData(listId, false, items))
        }
        rebuildDisplayList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = displayList.size

    override fun getItemViewType(position: Int): Int {
        return when (displayList[position]) {
            is ListItem.Header -> VIEW_TYPE_HEADER
            is ListItem.Item -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ItemHeaderBinding.inflate(inflater, parent, false)
                HeaderViewHolder(binding)
            }
            else -> {
                val binding = ItemRowBinding.inflate(inflater, parent, false)
                ItemViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = displayList[position]) {
            is ListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is ListItem.Item -> (holder as ItemViewHolder).bind(item.uiItem)
        }
    }

    private fun rebuildDisplayList() {
        displayList.clear()
        for (group in groups) {
            displayList.add(ListItem.Header(group.listId, group.isCollapsed, group.items.size))
            if (!group.isCollapsed) {
                // If expanded, show items
                group.items.forEach { displayList.add(ListItem.Item(it)) }
            }
        }
    }

    private fun toggleGroup(listId: Int) {
        // Find the group and toggle its collapse state
        val group = groups.find { it.listId == listId }
        group?.let {
            it.isCollapsed = !it.isCollapsed
            rebuildDisplayList()
            notifyDataSetChanged()
        }
    }

    inner class HeaderViewHolder(private val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(header: ListItem.Header) {
            val context = binding.headerText.context

            binding.headerText.text = context.getString(R.string.list_header_text, header.listId)

            val iconRes = if (header.isCollapsed) {
                R.drawable.ic_arrow_up
            } else {
                R.drawable.ic_arrow_down
            }
            binding.headerIndicator.setImageResource(iconRes)
            binding.itemCountText.text = context.getString(R.string.item_count, header.itemCount)

            // Set a click listener to toggle the collapse state
            binding.root.setOnClickListener {
                toggleGroup(header.listId)
            }
        }
    }

    inner class ItemViewHolder(private val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UiItem) {
            binding.textView.text = item.name
        }
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    data class GroupData(
        val listId: Int,
        var isCollapsed: Boolean,
        val items: List<UiItem>
    )
}
