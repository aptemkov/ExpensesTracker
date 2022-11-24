package com.github.aptemkov.expensestracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.aptemkov.expensestracker.domain.Item
import com.github.aptemkov.expensestracker.domain.getFormattedPrice
import com.github.aptemkov.expensestracker.databinding.ItemListItemBinding
import com.github.aptemkov.expensestracker.domain.Item.Companion.EXPENSE
import com.github.aptemkov.expensestracker.domain.Item.Companion.INCOME

class ItemListAdapter(private val onItemClicked: (Item) -> Unit) :
    ListAdapter<Item, ItemListAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class ItemViewHolder(private var binding: ItemListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.apply {
                itemCategory.text = item.itemCategory
                itemTitle.text = item.itemCategory
                //itemQuantity.text = item.isCompulsory.toString()
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

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.itemCategory == newItem.itemCategory
            }
        }
    }


}