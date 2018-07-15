package com.example.ayushgupta.ktmy19.presenter

import android.view.View
import com.example.ayushgupta.ktmy19.beans.UserPassBeans

interface UserPassPresenter {
    fun loginWithEmail(userPassBeans: UserPassBeans, view: View, isAdmin: Boolean)
}