package com.example.ayushgupta.ktmy19.beans

import android.net.Uri
import java.io.Serializable

data class UserInfoBeans(val name: String, val email: String, val photoUrl: Uri) : Serializable