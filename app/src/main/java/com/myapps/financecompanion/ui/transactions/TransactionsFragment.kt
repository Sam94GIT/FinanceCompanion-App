package com.myapps.financecompanion.ui.transactions


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.myapps.financecompanion.data.Transaction
import com.myapps.financecompanion.databinding.FragmentTransactionsBinding
import com.myapps.financecompanion.viewmodel.TransactionViewModel
import androidx.navigation.fragment.findNavController
import com.myapps.financecompanion.R

class TransactionsFragment : Fragment() {

    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionViewModel by viewModels()
    private lateinit var adapter: TransactionAdapter

    private var allTransactions = listOf<Transaction>()
    private var currentFilter = "all"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        adapter = TransactionAdapter { transaction ->
            showDeleteDialog(transaction)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // Observe transactions
        viewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            allTransactions = transactions
            applyFilterAndSearch()
        }

        // Filter buttons
        binding.btnAll.setOnClickListener {
            currentFilter = "all"
            applyFilterAndSearch()
        }
        binding.btnIncome.setOnClickListener {
            currentFilter = "income"
            applyFilterAndSearch()
        }
        binding.btnExpense.setOnClickListener {
            currentFilter = "expense"
            applyFilterAndSearch()
        }

        // Search
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { applyFilterAndSearch() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // FAB navigates to Add Transaction screen
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.addTransactionFragment)
        }
    }

    private fun applyFilterAndSearch() {
        val searchQuery = binding.etSearch.text.toString().lowercase()

        var filtered = allTransactions

        // Apply type filter
        if (currentFilter != "all") {
            filtered = filtered.filter { it.type == currentFilter }
        }

        // Apply search
        if (searchQuery.isNotEmpty()) {
            filtered = filtered.filter {
                it.category.lowercase().contains(searchQuery) ||
                        it.note.lowercase().contains(searchQuery)
            }
        }

        // Show empty state
        if (filtered.isEmpty()) {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.layoutEmpty.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }

        adapter.submitList(filtered)
    }

    private fun showDeleteDialog(transaction: Transaction) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Transaction")
            .setMessage("Are you sure you want to delete this transaction?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.delete(transaction)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}