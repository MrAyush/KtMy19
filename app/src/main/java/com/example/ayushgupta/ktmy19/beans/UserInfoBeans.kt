package com.example.ayushgupta.ktmy19.beans

import android.net.Uri
import java.io.Serializable

data class UserInfoBeans(var name: String, var email: String, var photoUrl: Uri) : Serializable