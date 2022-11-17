package com.github.aptemkov.expensestracker

import android.net.wifi.p2p.WifiP2pManager.ActionListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.aptemkov.expensestracker.databinding.CategoryItemBinding
import com.github.aptemkov.expensestracker.domain.Item

interface CategoryActionListener {
    fun onClick(category: String)
}

class CategoryAdapter(private val actionListener: CategoryActionListener)
    : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(), View.OnClickListener {

    override fun onClick(v: View?) {
        val category = v?.tag as String
        actionListener.onClick(category)
    }

    var categories: List<String> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryItemBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val current = categories[position]
        holder.itemView.tag = current
        holder.binding.categoryName.text = current
    }

    override fun getItemCount(): Int = categories.size

    class CategoryViewHolder(val binding: CategoryItemBinding)
        : RecyclerView.ViewHolder(binding.root)

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