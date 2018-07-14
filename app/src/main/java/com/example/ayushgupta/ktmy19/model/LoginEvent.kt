package com.example.ayushgupta.ktmy19.model


import android.view.View
import android.widget.ProgressBar
import com.example.ayushgupta.ktmy19.LoginFrag
import com.example.ayushgupta.ktmy19.R
import com.example.ayushgupta.ktmy19.beans.UserPassBeans
import com.example.ayushgupta.ktmy19.presenter.UserPassPresenter
import com.google.firebase.auth.FirebaseAuth

class LoginEvent(private val loginFrag: LoginFrag) : UserPassPresenter {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var msg: String
    private lateinit var progressBar: ProgressBar

    override fun loginWithEmail(userPassBeans: UserPassBeans, view: View) {
        progressBar = view.findViewById(R.id.pb1)
        progressBar.visibility = View.VISIBLE
        mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(userPassBeans.username, userPassBeans.password).addOnCompleteListener {
            msg = if (it.isSuccessful) {
                "Login Successful."
            } else {
                "Login failed."
            }
            progressBar.visibility = View.GONE
            loginFrag.onConnectionResults(msg)
        }
    }
}