package com.myapps.financecompanion.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val type: String,       // "income" or "expense"
    val category: String,
    val date: Long,         // store as timestamp (milliseconds)
    val note: String
)