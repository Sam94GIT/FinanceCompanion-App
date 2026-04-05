package com.myapps.financecompanion.ui.home


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.myapps.financecompanion.databinding.FragmentHomeBinding
import com.myapps.financecompanion.ui.transactions.TransactionAdapter
import com.myapps.financecompanion.viewmodel.TransactionViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recent transactions recycler
        val adapter = TransactionAdapter {}
        binding.rvRecent.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecent.adapter = adapter

        // Observe income
        viewModel.totalIncome.observe(viewLifecycleOwner) {
            val income = it ?: 0.0
            binding.tvIncome.text = "₹${"%.2f".format(income)}"
            updateBalance()
        }

        // Observe expense
        viewModel.totalExpense.observe(viewLifecycleOwner) {
            val expense = it ?: 0.0
            binding.tvExpense.text = "₹${"%.2f".format(expense)}"
            updateBalance()
        }

        // Observe recent transactions
        viewModel.recentTransactions.observe(viewLifecycleOwner) { transactions ->
            adapter.submitList(transactions)
        }
    }

    private fun updateBalance() {
        val income = viewModel.totalIncome.value ?: 0.0
        val expense = viewModel.totalExpense.value ?: 0.0
        val balance = income - expense
        binding.tvBalance.text = "₹${"%.2f".format(balance)}"
        binding.tvBalance.setTextColor(
            if (balance >= 0) Color.WHITE
            else Color.parseColor("#EF9A9A")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}