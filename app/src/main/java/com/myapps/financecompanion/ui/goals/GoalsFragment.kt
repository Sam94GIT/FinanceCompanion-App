package com.myapps.financecompanion.ui.goals


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.myapps.financecompanion.databinding.FragmentGoalsBinding
import com.myapps.financecompanion.viewmodel.TransactionViewModel
import java.util.Calendar

class GoalsFragment : Fragment() {

    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionViewModel by viewModels()

    // SharedPreferences key
    private val PREF_NAME = "finance_prefs"
    private val KEY_BUDGET = "monthly_budget"

    private val tips = listOf(
        "Track every small expense — coffee and snacks add up fast.",
        "Follow the 50/30/20 rule: 50% needs, 30% wants, 20% savings.",
        "Wait 24 hours before making any unplanned purchase.",
        "Cook at home at least 4 days a week to cut food costs.",
        "Cancel subscriptions you haven't used in the last month.",
        "Set a weekly spending limit for entertainment.",
        "Always compare prices before buying electronics or appliances."
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load saved budget and show it
        val savedBudget = getSavedBudget()
        if (savedBudget > 0) {
            binding.etBudget.setText(savedBudget.toString())
        }

        // Show a random tip
        binding.tvTip.text = tips.random()

        // Save budget button
        binding.btnSetBudget.setOnClickListener {
            val input = binding.etBudget.text.toString()
            if (input.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a budget amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            saveBudget(input.toDouble())
            Toast.makeText(requireContext(), "Budget saved!", Toast.LENGTH_SHORT).show()
            updateProgress()
        }

        // Observe this month's expenses
        observeMonthlyExpenses()
    }

    private fun observeMonthlyExpenses() {
        val calendar = Calendar.getInstance()

        // Start of this month
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        val monthStart = calendar.timeInMillis

        // End of this month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        val monthEnd = calendar.timeInMillis

        viewModel.getExpenseBetweenDates(monthStart, monthEnd)
            .observe(viewLifecycleOwner) { spent ->
                val spentAmount = spent ?: 0.0
                binding.tvSpent.text = "₹${"%.0f".format(spentAmount)}"
                updateProgressWithSpent(spentAmount)
            }
    }

    private fun updateProgress() {
        val spentText = binding.tvSpent.text.toString()
            .replace("₹", "").toDoubleOrNull() ?: 0.0
        updateProgressWithSpent(spentText)
    }

    private fun updateProgressWithSpent(spent: Double) {
        val budget = getSavedBudget()

        binding.tvBudgetAmount.text = "₹${"%.0f".format(budget)}"

        if (budget <= 0) return

        val remaining = budget - spent
        val percent = ((spent / budget) * 100).toInt().coerceAtMost(100)

        binding.tvRemaining.text = "₹${"%.0f".format(remaining.coerceAtLeast(0.0))}"
        binding.tvPercent.text = "$percent% of budget used"
        binding.progressBudget.progress = percent

        // Change progress bar color based on usage
        when {
            percent >= 100 -> {
                binding.progressBudget.progressTintList =
                    android.content.res.ColorStateList.valueOf(Color.parseColor("#C62828"))
                showWarning("🚨 You have exceeded your monthly budget!", "#FFEBEE", "#C62828")
            }
            percent >= 80 -> {
                binding.progressBudget.progressTintList =
                    android.content.res.ColorStateList.valueOf(Color.parseColor("#FF9800"))
                showWarning("⚠️ You've used $percent% of your budget. Slow down!", "#FFF8E1", "#E65100")
            }
            else -> {
                binding.progressBudget.progressTintList =
                    android.content.res.ColorStateList.valueOf(Color.parseColor("#4CAF50"))
                binding.tvWarning.visibility = View.GONE
            }
        }
    }

    private fun showWarning(message: String, bgColor: String, textColor: String) {
        binding.tvWarning.apply {
            text = message
            setTextColor(Color.parseColor(textColor))
            setBackgroundColor(Color.parseColor(bgColor))
            visibility = View.VISIBLE
        }
    }

    private fun saveBudget(amount: Double) {
        val prefs = requireContext().getSharedPreferences(PREF_NAME, android.content.Context.MODE_PRIVATE)
        prefs.edit().putFloat(KEY_BUDGET, amount.toFloat()).apply()
    }

    private fun getSavedBudget(): Double {
        val prefs = requireContext().getSharedPreferences(PREF_NAME, android.content.Context.MODE_PRIVATE)
        return prefs.getFloat(KEY_BUDGET, 0f).toDouble()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}