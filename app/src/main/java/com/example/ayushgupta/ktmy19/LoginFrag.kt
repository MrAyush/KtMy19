package com.example.ayushgupta.ktmy19

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.example.ayushgupta.ktmy19.beans.UserPassBeans
import com.example.ayushgupta.ktmy19.model.LoginEvent
import com.example.ayushgupta.ktmy19.view.UserPassView

class LoginFrag : Fragment(), UserPassView {
    override fun onConnectionResults(msg: String, isSuccess: Boolean, isAdmin: Boolean) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        val intent = if (isSuccess && !isAdmin) {
            Intent(this.activity, UserActivity::class.java)
        } else{
            Intent(Intent.ACTION_CALL)
        }
        startActivity(intent)
    }

    private lateinit var userPassBeans: UserPassBeans
    private lateinit var btn: Button
    private lateinit var let1: EditText
    private lateinit var let2: EditText
    private lateinit var radioButton: RadioButton

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater?.inflate(R.layout.loginpage, container, false)
        btn = view!!.findViewById(R.id.login)
        let1 = view.findViewById(R.id.let1)
        let2 = view.findViewById(R.id.let2)
        radioButton = view.findViewById(R.id.admin)

        btn.setOnClickListener {
            val cManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cManager.activeNetworkInfo
            val isConnected = nInfo?.isConnected == true
            if (isConnected) {
                if (let1.text.toString() == "" || let2.text.toString() == "") {
                    Toast.makeText(activity, "Email/Password field is empty", Toast.LENGTH_SHORT).show()
                } else {
                    userPassBeans = UserPassBeans(let1.text.toString(), let2.text.toString())
                    val loginEvent = LoginEvent(this)
                    loginEvent.loginWithEmail(userPassBeans, view)
                    if (radioButton.isChecked) {
                        loginEvent.checkAdmin()
                    }
                }
            } else {
                Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}