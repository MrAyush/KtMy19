package com.example.ayushgupta.ktmy19.model

import com.example.ayushgupta.ktmy19.presenter.LogoutPresenter
import com.google.firebase.auth.FirebaseAuth

class LogoutEvent : LogoutPresenter {
    override fun logoutUser() {
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
    }
}