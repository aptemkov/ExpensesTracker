package com.github.aptemkov.expensestracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.aptemkov.expensestracker.data.Item
import com.github.aptemkov.expensestracker.data.getFormattedPrice
import com.github.aptemkov.expensestracker.databinding.FragmentItemDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat

class ItemDetailFragment : Fragment() {
    private val navigationArgs: ItemDetailFragmentArgs by navArgs()
    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExpensesViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as ExpensesApplication).database.itemDao()
        )
    }

    lateinit var item: Item

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) {
            item = it
            bind(item)
        }


    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteItem()
            }
            .show()
    }

    private fun deleteItem() {
        viewModel.deleteItem(item)
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }


    private fun bind(item: Item) {
        binding.apply {
            itemCategory.text = item.itemCategory
            itemPrice.text = item.getFormattedPrice()
            itemIsCompulsory.text = item.isCompulsory.toString()

            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            itemDate.text = simpleDateFormat.format(item.date)

            sellItem.isEnabled = viewModel.isSellAvailable(item)
            sellItem.setOnClickListener { viewModel.sellItem(item) }

            deleteItem.setOnClickListener { showConfirmationDialog() }

            editItem.setOnClickListener { editItem() }
        }
    }

    private fun editItem() {
        val action = ItemDetailFragmentDirections.actionItemDetailFragment2ToAddItemFragment(
            item.id,
            getString(R.string.edit_fragment_title)
        )
        this.findNavController().navigate(action)
    }
}
