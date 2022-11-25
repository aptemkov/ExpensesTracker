package com.github.aptemkov.expensestracker

import android.os.Bundle
import android.view.*
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.aptemkov.expensestracker.databinding.FragmentItemListBinding
import com.github.aptemkov.expensestracker.domain.Item.Companion.ALL_TRANSACTIONS
import com.github.aptemkov.expensestracker.domain.Item.Companion.EXPENSE
import com.github.aptemkov.expensestracker.domain.Item.Companion.INCOME

class ItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ItemListAdapter

    private val viewModel: ExpensesViewModel by activityViewModels {
        ExpensesViewModelFactory(
            (activity?.application as ExpensesApplication).database.itemDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.floatingActionButton.setOnClickListener {
            val action = ItemListFragmentDirections.actionNavigationListToAddItemFragment(
                -1,
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }

        adapter = ItemListAdapter {
            val action = ItemListFragmentDirections.actionNavigationListToItemDetailFragment2(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && binding.floatingActionButton.isShown) binding.floatingActionButton.hide()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) binding.floatingActionButton.show()
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }
    }


    private fun observeFilter() = with(binding) {
        lifecycleScope.launchWhenCreated {
            viewModel.transactionType.collect { filter ->
                when (filter) {
                    "Overall" -> {
                        adapter.submitList(viewModel.allItems.value)
                        binding.recyclerView.adapter = adapter

                    }
                    "Income" -> {
                        adapter.submitList(viewModel.allItems.value?.filter { it.transactionType == filter })
                        binding.recyclerView.adapter = adapter
                    }
                    "Expense" -> {
                        adapter.submitList(viewModel.allItems.value?.filter { it.transactionType == filter })
                        binding.recyclerView.adapter = adapter
                    }
                }
                viewModel.getAllTransaction(filter)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val item = menu.findItem(R.id.spinner)
        val spinner = item.actionView as Spinner

        val adapter = activity?.applicationContext?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.transaction_types,
                R.layout.item_toolbar_spinner
            )
        }
        adapter?.setDropDownViewResource(R.layout.item_toolbar_spinner)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                lifecycleScope.launchWhenStarted {
                    when (position) {
                        0 -> {
                            viewModel.setAllTransactions()
                            (view as TextView).setTextColor(resources.getColor(R.color.dynamic_main_color))
                        }
                        1 -> {
                            viewModel.setIncomeTransactions()
                            (view as TextView).setTextColor(resources.getColor(R.color.dynamic_main_color))
                        }
                        2 -> {
                            viewModel.setExpenseTransactions()
                            (view as TextView).setTextColor(resources.getColor(R.color.dynamic_main_color))
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                lifecycleScope.launchWhenStarted {
                    viewModel.setAllTransactions()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                //TODO()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
