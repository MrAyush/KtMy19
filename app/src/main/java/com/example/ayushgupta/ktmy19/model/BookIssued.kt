package com.example.ayushgupta.ktmy19.model

import android.util.Log
import com.example.ayushgupta.ktmy19.UserActivity
import com.example.ayushgupta.ktmy19.beans.BookBeans
import com.example.ayushgupta.ktmy19.presenter.BookIssuePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookIssued(private val userActivity: UserActivity) : BookIssuePresenter {

    private var dBase: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun getIssuedBooks(): Boolean {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        dBase.collection("IssuedBooks").document(uid!!).get()
                .addOnSuccessListener {
                    val bookBeans = it.toObject(BookBeans::class.java)
                    Log.w("Book Details","${bookBeans?.preDues.toString()} ${bookBeans?.nBooks.toString()} ${bookBeans?.books?.name}" +
                            "${bookBeans?.books?.dueDate} ${bookBeans?.books?.issuedDate} ${bookBeans?.books?.bookRef}")
                    userActivity.setBooks(bookBeans!!)
                }.addOnFailureListener {
                    it.printStackTrace()
                }
        return true
    }
}