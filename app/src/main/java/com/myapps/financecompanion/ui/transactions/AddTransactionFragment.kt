package com.myapps.financecompanion.ui.transactions


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.myapps.financecompanion.R
import com.myapps.financecompanion.data.Transaction
import com.myapps.financecompanion.databinding.FragmentAddTransactionBinding
import com.myapps.financecompanion.viewmodel.TransactionViewModel
import java.util.Calendar

class AddTransactionFragment : Fragment() {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionViewModel by viewModels()

    private var selectedDate = System.currentTimeMillis()

    private val categories = listOf(
        "Food", "Transport", "Shopping", "Bills",
        "Entertainment", "Health", "Education", "Salary", "Other"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup category spinner
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerCategory.adapter = adapter
        binding.spinnerCategory.setPopupBackgroundResource(android.R.color.black)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Date picker
        binding.btnDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    selectedDate = calendar.timeInMillis
                    binding.btnDate.text = "$day/${month + 1}/$year"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Save button
        binding.btnSave.setOnClickListener {
            saveTransaction()
        }
    }

    private fun saveTransaction() {
        val amountText = binding.etAmount.text.toString()

        if (amountText.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter an amount", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDouble()
        val type = if (binding.rbIncome.isChecked) "income" else "expense"
        val category = binding.spinnerCategory.selectedItem.toString()
        val note = binding.etNote.text.toString()

        val transaction = Transaction(
            amount = amount,
            type = type,
            category = category,
            date = selectedDate,
            note = note
        )

        viewModel.insert(transaction)
        Toast.makeText(requireContext(), "Transaction saved!", Toast.LENGTH_SHORT).show()

        // Clear the form
        binding.etAmount.text?.clear()
        binding.etNote.text?.clear()
        binding.rbExpense.isChecked = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}