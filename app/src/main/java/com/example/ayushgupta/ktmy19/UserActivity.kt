package com.example.ayushgupta.ktmy19

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.ayushgupta.ktmy19.beans.BookBeans
import com.example.ayushgupta.ktmy19.model.BookAdapter
import com.example.ayushgupta.ktmy19.model.BookIssued
import com.example.ayushgupta.ktmy19.model.LogoutEvent
import com.example.ayushgupta.ktmy19.view.BookView
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.app_bar_user.*

class UserActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, BookView {

    lateinit var bookView: RecyclerView
    lateinit var pb2: ProgressBar
    lateinit var noBook: TextView
    lateinit var previousDues: TextView

    override fun setBooks(bookBeans: BookBeans) {
        //Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
        val books = "No of books ${bookBeans.nBooks}"
        val dues = "Previous dues ${bookBeans.preDues}"
        noBook.text = books
        previousDues.text = dues
        pb2.visibility = View.GONE
        if (bookBeans.nBooks == 0) {
            //No books Issued
            Toast.makeText(this, "No books are issued by you", Toast.LENGTH_LONG).show()
        } else {
            //Some books are issued
            bookView.adapter = BookAdapter(bookBeans)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        bookView = findViewById(R.id.books_view)
        pb2 = findViewById(R.id.pb2)
        noBook = findViewById(R.id.no_book)
        previousDues = findViewById(R.id.dues)

        val lManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bookView.layoutManager = lManager

        if (!checkConnection()) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
        } else {
            BookIssued(this).getIssuedBooks()
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.user, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_logout -> {
                if (checkConnection()) {
                    LogoutEvent().logoutUser()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    this.finish()
                } else
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun checkConnection(): Boolean {
        val cManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }
}
