package com.myapps.financecompanion.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.myapps.financecompanion.data.Transaction
import com.myapps.financecompanion.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TransactionRepository(application)

    val allTransactions = repository.allTransactions

    fun insert(transaction: Transaction) = viewModelScope.launch {
        repository.insert(transaction)
    }

    fun update(transaction: Transaction) = viewModelScope.launch {
        repository.update(transaction)
    }

    fun delete(transaction: Transaction) = viewModelScope.launch {
        repository.delete(transaction)
    }

    // new for Home Dashboard
    val totalIncome = repository.totalIncome
    val totalExpense = repository.totalExpense
    val recentTransactions = repository.recentTransactions
    val expenseByCategory = repository.expenseByCategory

    // new for Insights
    val topSpendingCategory = repository.topSpendingCategory
    val topExpenses = repository.topExpenses

    fun getExpenseBetweenDates(startDate: Long, endDate: Long) =
        repository.getExpenseBetweenDates(startDate, endDate)
}