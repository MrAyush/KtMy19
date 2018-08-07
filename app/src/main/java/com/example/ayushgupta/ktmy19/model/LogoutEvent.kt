package com.example.ayushgupta.ktmy19.model

import com.example.ayushgupta.ktmy19.UserActivity
import com.example.ayushgupta.ktmy19.presenter.LogoutPresenter
import com.google.firebase.auth.FirebaseAuth

class LogoutEvent : LogoutPresenter {
    override fun logoutUser(userActivity: UserActivity) {
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
        mAuth.addAuthStateListener {
            userActivity.onLogout()
        }
    }
}