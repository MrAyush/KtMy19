package com.example.ayushgupta.ktmy19.model

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.ayushgupta.ktmy19.R
import com.example.ayushgupta.ktmy19.beans.BookBeans

class BookAdapter(private val bookBeans: BookBeans) : RecyclerView.Adapter<BookHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BookHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.book_cardview,parent,false)
        return BookHolder(view)
    }

    override fun getItemCount(): Int {
        return bookBeans.nBooks
    }

    override fun onBindViewHolder(holder: BookHolder?, position: Int) {
        val name = "Book name: ${bookBeans.books.name[position]}"
        holder?.bookName?.text = name
        val dues = "Due date: ${bookBeans.books.dueDate[position].toDate()}"
        holder?.dueDate?.text = dues
        val issued = "Issued date: ${bookBeans.books.issuedDate[position].toDate()}"
        holder?.issuedDate?.text = issued
        val reference = "Ref. no: ${bookBeans.books.bookRef[position]}"
        holder?.refNo?.text = reference
    }
}