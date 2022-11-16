package com.github.aptemkov.expensestracker

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.aptemkov.expensestracker.databinding.CategoryItemBinding
import com.github.aptemkov.expensestracker.domain.Item

class CategoryAdapter(private val onItemClicked: (String) -> Unit) :
    //FIX()
    ListAdapter<String, CategoryAdapter.CategoryViewHolder>(DiffCallback) {

    var categories: List<String> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val current = categories[position]
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    override fun getItemCount(): Int = categories.size

    class CategoryViewHolder(val binding: CategoryItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(category: String) {
                binding.categoryName.text = category
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