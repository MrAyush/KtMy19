package com.example.ayushgupta.ktmy19.beans

import com.google.firebase.Timestamp

class BooksDetails {
    lateinit var bookRef: List<String>
    lateinit var dueDate: List<Timestamp>
    lateinit var name: List<String>
    lateinit var issuedDate: List<Timestamp>
}