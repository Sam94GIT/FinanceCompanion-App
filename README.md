# 💰 Finance Companion

A personal finance companion Android app that helps users track daily money habits, monitor spending patterns, and stay within budget goals.

Built as part of a mobile development internship assessment for Zorvyn.

---


## ✨ Features

### 🏠 Home Dashboard
- Total balance overview with income and expenses summary
- Quick navigation to all sections
- Recent transactions list

### 💳 Transaction Management
- Add income and expense transactions
- Select category, date, and add notes
- View full transaction history
- Filter by income or expense
- Search transactions by category or note
- Long press to delete with confirmation dialog

### 📊 Insights
- This week vs last week spending comparison
- Top spending category with emoji indicator
- Biggest 3 expenses
- Monthly spending across last 4 months

### 🎯 Monthly Budget Goal
- Set a monthly budget limit
- Real time progress bar showing spending vs budget
- Warning at 80% usage, alert at 100%
- Remaining balance tracker
- Random saving tips

---

## 🏗️ Architecture & Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| Architecture | MVVM (Model-View-ViewModel) |
| Database | Room DB (SQLite) |
| State Management | LiveData + ViewModel |
| Async Operations | Kotlin Coroutines |
| Navigation | Jetpack Navigation Component |
| Annotation Processing | KSP |
| UI | XML Layouts, Material3, CardView |
| Budget Storage | SharedPreferences |

---

---

## 🚀 How to Run

1. Clone the repository
   git clone https://github.com/Sam94GIT/FinanceCompanion-App.git
2. Open in **Android Studio** (Hedgehog or later)
3. Wait for Gradle sync to complete
4. Run on an emulator or physical device with **API 24+**

---

## 💡 Assumptions Made

- App uses **local Room database** — no backend or internet required
- Monthly budget is stored in **SharedPreferences** and persists across sessions
- Transactions are identified by **auto-generated ID**
- Date is stored as a **Unix timestamp** in milliseconds
- The app targets **Indian Rupee (₹)** as the default currency
- Categories are predefined: Food, Transport, Shopping, Bills, Entertainment, Health, Education, Salary, Other
- Long press on a transaction shows a delete confirmation dialog

---

## 📋 Assignment Requirements Coverage

| Requirement | Status |
|---|---|
| Home Dashboard with summary | ✅ Done |
| Visual element on home | ✅ Income/Expense cards |
| Add Transaction | ✅ Done |
| View Transaction History | ✅ Done |
| Edit/Delete Transactions | ✅ Done |
| Filter and Search | ✅ Done |
| Goal / Challenge Feature | ✅ Monthly Budget Tracker |
| Insights Screen | ✅ Done |
| Smooth Mobile UX | ✅ Done |
| Empty States | ✅ Done |
| Local Data Handling | ✅ Room DB |
| State Management | ✅ MVVM + LiveData |

---

## 👨‍💻 Developer

**Samiksha**
Mobile Development Internship Assignment — Zorvyn