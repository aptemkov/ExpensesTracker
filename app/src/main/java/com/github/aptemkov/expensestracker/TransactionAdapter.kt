package com.github.aptemkov.expensestracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.aptemkov.expensestracker.databinding.TransactionItemBinding
import com.github.aptemkov.expensestracker.domain.transaction.Transaction
import com.github.aptemkov.expensestracker.domain.transaction.Transaction.Companion.EXPENSE
import com.github.aptemkov.expensestracker.domain.transaction.Transaction.Companion.INCOME
import com.github.aptemkov.expensestracker.domain.transaction.getFormattedPrice

class TransactionAdapter(private val listener: Listener) :
    ListAdapter<Transaction, TransactionAdapter.ItemViewHolder>(DiffCallback), View.OnClickListener {

    override fun onClick(v: View?) {
        val transaction = v?.tag as Transaction
        listener.onDetailInfo(transaction.id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TransactionItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }

    class ItemViewHolder(private var binding: TransactionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            binding.apply {
                root.tag = transaction

                itemCategory.text = transaction.transactionCategory
                itemDescription.text = transaction.transactionDescription
                when (transaction.transactionType) {
                    EXPENSE -> {
                        val color: Int = when (transaction.isCompulsory) {
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
                        itemPrice.text = "- ".plus(transaction.getFormattedPrice())
                    }
                    INCOME -> {
                        val color = ContextCompat.getColor(
                            itemPrice.context,
                            R.color.income_color
                        )
                        itemPrice.setTextColor(color)
                        itemPrice.text = "+ ".plus(transaction.getFormattedPrice())
                    }
                }
            }
        }
    }

    interface Listener {
        fun onDetailInfo(itemId: Int)
    }

    object DiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldTransaction: Transaction, newTransaction: Transaction): Boolean {
            return oldTransaction.id == newTransaction.id
        }

        override fun areContentsTheSame(oldTransaction: Transaction, newTransaction: Transaction): Boolean {
            return oldTransaction == newTransaction
        }
    }
}