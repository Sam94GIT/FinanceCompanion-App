package com.myapps.financecompanion.ui.insights


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.myapps.financecompanion.databinding.FragmentInsightsBinding
import com.myapps.financecompanion.ui.transactions.TransactionAdapter
import com.myapps.financecompanion.viewmodel.TransactionViewModel
import java.util.Calendar

class InsightsFragment : Fragment() {

    private var _binding: FragmentInsightsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInsightsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopExpenses()
        setupWeekComparison()
        setupTopCategory()
        setupMonthlyCards()
    }

    private fun setupTopExpenses() {
        val adapter = TransactionAdapter {}
        binding.rvTopExpenses.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTopExpenses.adapter = adapter
        viewModel.topExpenses.observe(viewLifecycleOwner) { expenses ->
            adapter.submitList(expenses)
        }
    }

    private fun setupWeekComparison() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        val thisWeekStart = calendar.timeInMillis
        val thisWeekEnd = System.currentTimeMillis()

        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        val lastWeekStart = calendar.timeInMillis
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        val lastWeekEnd = calendar.timeInMillis

        viewModel.getExpenseBetweenDates(thisWeekStart, thisWeekEnd)
            .observe(viewLifecycleOwner) { amount ->
                binding.tvThisWeek.text = "₹${"%.0f".format(amount ?: 0.0)}"
                updateComparison()
            }

        viewModel.getExpenseBetweenDates(lastWeekStart, lastWeekEnd)
            .observe(viewLifecycleOwner) { amount ->
                binding.tvLastWeek.text = "₹${"%.0f".format(amount ?: 0.0)}"
                updateComparison()
            }
    }

    private fun updateComparison() {
        val thisWeek = binding.tvThisWeek.text.toString()
            .replace("₹", "").toDoubleOrNull() ?: 0.0
        val lastWeek = binding.tvLastWeek.text.toString()
            .replace("₹", "").toDoubleOrNull() ?: 0.0

        if (lastWeek == 0.0) {
            binding.tvComparison.text = "No data from last week"
            return
        }

        val diff = ((thisWeek - lastWeek) / lastWeek * 100).toInt()
        when {
            diff > 0 -> {
                binding.tvComparison.text = "⚠️ $diff% more than last week"
                binding.tvComparison.setTextColor(Color.parseColor("#C62828"))
            }
            diff < 0 -> {
                binding.tvComparison.text = "✅ ${-diff}% less than last week"
                binding.tvComparison.setTextColor(Color.parseColor("#2E7D32"))
            }
            else -> {
                binding.tvComparison.text = "Same as last week"
                binding.tvComparison.setTextColor(Color.parseColor("#9E9E9E"))
            }
        }
    }

    private fun setupTopCategory() {
        viewModel.topSpendingCategory.observe(viewLifecycleOwner) { cat ->
            if (cat == null) return@observe
            binding.tvTopCategory.text = cat.category
            binding.tvTopCategoryAmount.text = "₹${"%.2f".format(cat.total)} total spent"
            binding.tvTopCategoryIcon.text = getCategoryEmoji(cat.category)
        }
    }

    private fun setupMonthlyCards() {
        val monthNames = listOf("Jan","Feb","Mar","Apr","May","Jun",
            "Jul","Aug","Sep","Oct","Nov","Dec")

        val monthViews = listOf(
            binding.tvMonth1, binding.tvMonth2,
            binding.tvMonth3, binding.tvMonth4
        )
        val amountViews = listOf(
            binding.tvMonthAmount1, binding.tvMonthAmount2,
            binding.tvMonthAmount3, binding.tvMonthAmount4
        )

        for (i in 3 downTo 0) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.MONTH, -i)
            val month = cal.get(Calendar.MONTH)

            cal.set(Calendar.DAY_OF_MONTH, 1)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            val start = cal.timeInMillis

            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
            cal.set(Calendar.HOUR_OF_DAY, 23)
            val end = cal.timeInMillis

            val index = 3 - i
            monthViews[index].text = monthNames[month]

            viewModel.getExpenseBetweenDates(start, end)
                .observe(viewLifecycleOwner) { amount ->
                    amountViews[index].text = "₹${"%.0f".format(amount ?: 0.0)}"
                }
        }
    }

    private fun getCategoryEmoji(category: String): String {
        return when (category) {
            "Food" -> "🍔"
            "Transport" -> "🚗"
            "Shopping" -> "🛍️"
            "Bills" -> "📄"
            "Entertainment" -> "🎬"
            "Health" -> "💊"
            "Education" -> "📚"
            "Salary" -> "💰"
            else -> "💳"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}