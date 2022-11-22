package com.github.aptemkov.expensestracker

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.aptemkov.expensestracker.databinding.FragmentAddItemBinding
import com.github.aptemkov.expensestracker.domain.Item
import java.util.*


class AddItemFragment : Fragment() {

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExpensesViewModel by activityViewModels {
        ExpensesViewModelFactory(
            (activity?.application as ExpensesApplication).database.itemDao()
        )
    }
    lateinit var item: Item
    private var date: Long? = null
    private var category = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId

        val categories = resources.getStringArray(R.array.expense_categories)

        val adapter = CategoryAdapter(object : CategoryActionListener {
            override fun onClick(categ: String) {
                binding.itemCategory.setText(categ, TextView.BufferType.SPANNABLE)
            }
        })
        val layoutManager =
            LinearLayoutManager(activity?.applicationContext, LinearLayoutManager.HORIZONTAL, false)
        adapter.categories = categories.toList()
        binding.categoryRv.apply {
            this.adapter = adapter
            this.layoutManager = layoutManager
        }

        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                item = selectedItem
                bind(item)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewItem()
            }
        }

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            date = calendar.timeInMillis
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

    private fun bind(item: Item) {
        binding.apply {
            itemCategory.setText(item.itemCategory, TextView.BufferType.SPANNABLE)
            itemPrice.setText(item.itemPrice.toString())
            itemIsCompulsory.isChecked = item.isCompulsory
            calendarView.date = item.date
            saveAction.setOnClickListener { updateItem() }
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.itemCategory.text.toString(),
            binding.itemPrice.text.toString(),
            if (date != null) date.toString() else binding.calendarView.date.toString()
        )
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                "", //TODO(transaction)
                binding.itemCategory.text.toString(),
                binding.itemPrice.text.toString(),
                binding.itemIsCompulsory.isChecked.toString(),
                if (date != null) date.toString() else binding.calendarView.date.toString()
            )
            goBack()
        } else {
            binding.itemCategory.error = getString(R.string.InputError)
            binding.itemPrice.error = getString(R.string.InputError)
        }
    }

    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.itemId,
                "", //TODO(transaction)
                this.binding.itemCategory.text.toString(),
                this.binding.itemPrice.text.toString(),
                this.binding.itemIsCompulsory.isChecked.toString(),
                if (date != null) date.toString() else binding.calendarView.date.toString()
            )
            goBack()
        } else {
            binding.itemCategory.error = getString(R.string.InputError)
            binding.itemPrice.error = getString(R.string.InputError)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.adding_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.done -> {
                binding.saveAction.callOnClick()
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun goBack() {
        findNavController().navigateUp()
    }
}
