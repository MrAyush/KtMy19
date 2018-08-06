package com.example.ayushgupta.ktmy19.model

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.ayushgupta.ktmy19.R

class BookHolder(v: View) : RecyclerView.ViewHolder(v) {
    var bookName: TextView = v.findViewById(R.id.book_name)
    var issuedDate: TextView = v.findViewById(R.id.issued_date)
    var dueDate: TextView = v.findViewById(R.id.due_date)
    var refNo: TextView = v.findViewById(R.id.ref_name)
}