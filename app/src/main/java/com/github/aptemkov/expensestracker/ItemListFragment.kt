package com.github.aptemkov.expensestracker

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.aptemkov.expensestracker.databinding.FragmentItemListBinding
import com.github.aptemkov.expensestracker.domain.Item
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        adapter = ItemListAdapter(object : ItemListAdapter.Listener {
            override fun onDetailInfo(itemId: Int) {
                val action =
                    ItemListFragmentDirections.actionNavigationListToItemDetailFragment2(itemId)
                findNavController().navigate(action)
            }
        })
        binding.recyclerView.adapter = adapter

        viewModel.items.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && binding.floatingActionButton.isShown) binding.floatingActionButton.hide()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) binding.floatingActionButton.show()
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun setupView() {
        binding.floatingActionButton.setOnClickListener {
            val action = ItemListFragmentDirections.actionNavigationListToAddItemFragment(
                -1,
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
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
                            Toast.makeText(activity?.applicationContext, "all", Toast.LENGTH_SHORT)
                                .show()
                            viewModel.setAllTransactions()
                        }
                        1 -> {
                            Toast.makeText(
                                activity?.applicationContext,
                                "expense",
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.setExpenseTransactions()
                        }
                        2 -> {
                            Toast.makeText(
                                activity?.applicationContext,
                                "income",
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.setIncomeTransactions()
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.setAllTransactions()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                showConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.clear_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteAll()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
