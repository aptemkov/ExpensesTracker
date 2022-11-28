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
import com.github.aptemkov.expensestracker.domain.Item.Companion.EXPENSE
import com.github.aptemkov.expensestracker.domain.Item.Companion.INCOME
import java.util.*


class AddItemFragment : Fragment() {

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var firstCategory: String

    private val viewModel: ExpensesViewModel by activityViewModels {
        ExpensesViewModelFactory(
            (activity?.application as ExpensesApplication).database.itemDao()
        )
    }

    lateinit var item: Item
    private var date: Long? = null
    private var transactionType = "expense"
    private var category: String? = null

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

        val expenseCategories = resources.getStringArray(R.array.expense_categories)
        val incomeCategories = resources.getStringArray(R.array.income_categories)

        val adapter = CategoryAdapter(object : CategoryActionListener {
            override fun onClick(newCategory: String) {
                category = newCategory
            }
        })
        val layoutManager =
            LinearLayoutManager(activity?.applicationContext, LinearLayoutManager.HORIZONTAL, false)
        adapter.categories = expenseCategories.toList()
        firstCategory = adapter.categories.first()
        binding.categoriesRv.apply {
            this.adapter = adapter
            this.layoutManager = layoutManager
        }


        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radiobutton_expense -> {
                    adapter.categories = expenseCategories.toList()
                    firstCategory = adapter.categories.first()
                    binding.categoriesRv.adapter = adapter
                    binding.itemIsCompulsory.visibility = View.VISIBLE
                    transactionType = EXPENSE
                }
                R.id.radiobutton_income -> {
                    adapter.categories = incomeCategories.toList()
                    firstCategory = adapter.categories.first()
                    binding.categoriesRv.adapter = adapter
                    binding.itemIsCompulsory.visibility = View.GONE
                    transactionType = INCOME
                }
            }
        }




        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                item = selectedItem
                bind(item)
                binding.radioGroup.check(
                    if(item.transactionType == EXPENSE) R.id.radiobutton_expense
                    else R.id.radiobutton_income
                )
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
            category = item.itemCategory
            itemPrice.setText(item.itemPrice.toString())
            itemIsCompulsory.isChecked = item.isCompulsory
            calendarView.date = item.date
            saveAction.setOnClickListener { updateItem() }
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            category ?: firstCategory,
            binding.itemPrice.text.toString(),
            if (date != null) date.toString() else binding.calendarView.date.toString()
        )
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                transactionType,
                category ?: firstCategory,
                binding.itemPrice.text.toString(),
                binding.itemIsCompulsory.isChecked.toString(),
                if (date != null) date.toString() else binding.calendarView.date.toString()
            )
            goBack()
        } else {
            binding.itemPrice.error = getString(R.string.InputError)
        }
    }

    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.itemId,
                transactionType,
                category ?: firstCategory,
                this.binding.itemPrice.text.toString(),
                this.binding.itemIsCompulsory.isChecked.toString(),
                if (date != null) date.toString() else binding.calendarView.date.toString()
            )
            goBack()
        } else {
            //binding.itemCategory.error = getString(R.string.InputError)
            binding.itemPrice.error = getString(R.string.InputError)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.adding_fragment_menu, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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
