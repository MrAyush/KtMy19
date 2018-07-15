package com.example.ayushgupta.ktmy19.model


import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.ayushgupta.ktmy19.LoginFrag
import com.example.ayushgupta.ktmy19.R
import com.example.ayushgupta.ktmy19.beans.UserPassBeans
import com.example.ayushgupta.ktmy19.presenter.UserPassPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginEvent(private val loginFrag: LoginFrag) : UserPassPresenter {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var msg: String
    private lateinit var progressBar: ProgressBar

    override fun loginWithEmail(userPassBeans: UserPassBeans, view: View, isAdmin: Boolean) {
        progressBar = view.findViewById(R.id.pb1)
        progressBar.visibility = View.VISIBLE
        mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(userPassBeans.username, userPassBeans.password).addOnCompleteListener {
            if (it.isSuccessful) {
                msg = "Login Successful."
                if (isAdmin) {
                    checkAdmin(mAuth)
                } else {
                    //TODO Navigate to the user dashboard
                }
            } else {
                msg = "Login failed."
            }
            progressBar.visibility = View.GONE
            loginFrag.onConnectionResults(msg)
        }
    }

    private fun checkAdmin(mAuth: FirebaseAuth) {
        val dbase = FirebaseFirestore.getInstance()
        dbase.collection("Admins").document("${mAuth.currentUser?.uid}")
                .get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val doc = it.result
                        if (doc.exists()) {
                            val isAdmin = doc.data?.getValue("isAdmin") as Boolean
                            if (isAdmin) {
                                Toast.makeText(loginFrag.activity, "user is admin", Toast.LENGTH_SHORT).show()
                                //TODO Navigate to the admin activity
                            } else {
                                Toast.makeText(loginFrag.activity, "user is not a admin", Toast.LENGTH_SHORT).show()
                            }
                            Log.w(TAG, "Value: " + doc.data.toString())
                        } else {
                            msg += "Error while checking admin"
                        }
                    } else {
                        msg += "doc doesn't exists"
                    }
                }.addOnFailureListener {
                    it.printStackTrace()
                }
    }
}