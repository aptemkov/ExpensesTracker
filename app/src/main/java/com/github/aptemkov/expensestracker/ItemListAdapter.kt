package com.github.aptemkov.expensestracker

import android.location.GnssAntennaInfo.Listener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.aptemkov.expensestracker.databinding.ItemListItemBinding
import com.github.aptemkov.expensestracker.domain.Item
import com.github.aptemkov.expensestracker.domain.Item.Companion.EXPENSE
import com.github.aptemkov.expensestracker.domain.Item.Companion.INCOME
import com.github.aptemkov.expensestracker.domain.getFormattedPrice

class ItemListAdapter(private val listener: Listener) :
    ListAdapter<Item, ItemListAdapter.ItemViewHolder>(DiffCallback), View.OnClickListener {

    override fun onClick(v: View?) {
        val item = v?.tag as Item
        listener.onDetailInfo(item.id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }

    class ItemViewHolder(private var binding: ItemListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.apply {
                root.tag = item

                itemCategory.text = item.itemCategory
                itemTitle.text = item.itemCategory
                when (item.transactionType) {
                    EXPENSE -> {
                        val color: Int = when (item.isCompulsory) {
                            true -> {
                                ContextCompat.getColor(
                                    itemPrice.context,
                                    R.color.expense_color
                                )
                            }
                            false -> {
                                ContextCompat.getColor(
                                    itemPrice.context,
                                    R.color.could_save_color
                                )
                            }

                        }
                        itemPrice.setTextColor(color)
                        itemPrice.text = "- ".plus(item.getFormattedPrice())
                    }
                    INCOME -> {
                        val color = ContextCompat.getColor(
                            itemPrice.context,
                            R.color.income_color
                        )
                        itemPrice.setTextColor(color)
                        itemPrice.text = "+ ".plus(item.getFormattedPrice())
                    }
                }
            }
        }
    }

    interface Listener {
        fun onDetailInfo(itemId: Int)
    }

    object DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }
}
