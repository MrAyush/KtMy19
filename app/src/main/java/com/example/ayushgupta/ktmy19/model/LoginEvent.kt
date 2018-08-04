package com.example.ayushgupta.ktmy19.model


import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.example.ayushgupta.ktmy19.LoginFrag
import com.example.ayushgupta.ktmy19.R
import com.example.ayushgupta.ktmy19.beans.UserInfoBeans
import com.example.ayushgupta.ktmy19.beans.UserPassBeans
import com.example.ayushgupta.ktmy19.presenter.UserPassPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginEvent(private val loginFrag: LoginFrag) : UserPassPresenter {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var msg: String
    private lateinit var progressBar: ProgressBar
    private lateinit var userInfoBeans: UserInfoBeans

    override fun loginWithEmail(userPassBeans: UserPassBeans, view: View) {
        msg = ""
        var isSuccess = false
        progressBar = view.findViewById(R.id.pb1)
        progressBar.visibility = View.VISIBLE
        mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(userPassBeans.username, userPassBeans.password).addOnCompleteListener {
            if (it.isSuccessful) {
                msg = "Login Successful."
                isSuccess = true
                mAuth = FirebaseAuth.getInstance()
                display(mAuth)
            } else {
                msg = "Login failed."
            }
            loginFrag.onConnectionResults(msg, isSuccess, false)
            progressBar.visibility = View.GONE
        }.addOnFailureListener {
            it.printStackTrace()
        }
    }

    private fun display(mAuth:  FirebaseAuth){
        val user = mAuth.currentUser
        if (user != null){
            //Log.w("name:", user.displayName)
            Log.w("email:", user.email)
            Log.w("pic:", user.photoUrl.toString())
        }
    }

    override fun checkAdmin() {
        mAuth = FirebaseAuth.getInstance()
        msg = ""
        var checkAdmin = false
        val dbase = FirebaseFirestore.getInstance()
        dbase.collection("Admins").document("${mAuth.currentUser?.uid}")
                .get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val doc = it.result
                        if (doc.exists()) {
                            checkAdmin = doc.data?.getValue("isAdmin") as Boolean
                            msg += "Admin: $checkAdmin"
                            if (checkAdmin) {
                                //TODO Navigate to the admin activity
                            } else {

                            }
                            Log.w(TAG, "Value: " + doc.data.toString())
                        } else {
                            msg = "Error while checking admin"
                        }
                    } else {
                        msg = "doc doesn't exists"
                    }
                    loginFrag.onConnectionResults(msg, false, checkAdmin)
                }.addOnFailureListener {
                    it.printStackTrace()
                }
    }
}