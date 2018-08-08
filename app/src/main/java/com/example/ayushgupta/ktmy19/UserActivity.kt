package com.example.ayushgupta.ktmy19

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.CalendarContract
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
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
import com.example.ayushgupta.ktmy19.view.LogoutView
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.app_bar_user.*
import java.util.*

class UserActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, BookView, LogoutView {
    //private lateinit var eventIds:MutableList<Long>
    private var long: Long = 0
    private val dateConst = 61200000L
    override fun onLogout() {
        pb2.visibility = View.GONE
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private lateinit var bookView: RecyclerView
    private lateinit var pb2: ProgressBar
    private lateinit var noBook: TextView
    private lateinit var previousDues: TextView

    override fun setBooks(bookBeans: BookBeans?) {
        //Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
        pb2.visibility = View.GONE
        if (bookBeans == null) {
            Toast.makeText(this, "Error while retrieving the record or your record is empty", Toast.LENGTH_LONG).show()
        } else {
            val books = "No of books ${bookBeans.nBooks}"
            val dues = "Previous dues ${bookBeans.preDues}"
            noBook.text = books
            previousDues.text = dues
            if (bookBeans.nBooks == 0) {
                //No books Issued
                Toast.makeText(this, "No books are issued by you", Toast.LENGTH_LONG).show()
            } else {
                //Some books are issued
                val bookAdapter = BookAdapter(bookBeans)
                bookView.adapter = bookAdapter
                val spf = getSharedPreferences("Write_to_cal", Context.MODE_PRIVATE)
                val isAllowed = spf.getBoolean("calWrite", true)
                val isWritten = spf.getBoolean("isWritten", false)
                //Delete
                if (!isAllowed && isWritten) {
                    deleteFromCalendar(bookBeans)
                    spf.edit().putBoolean("isWritten", false).apply()
                }
                //Add the Due date to calender
                if (isAllowed && !isWritten) {
                    writeToCalender(bookBeans)
                    spf.edit().putBoolean("isWritten", true).apply()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)
        Log.w("User Class", "Inside user class onCreate")

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        bookView = findViewById(R.id.books_view)
        pb2 = findViewById(R.id.pb2)
        noBook = findViewById(R.id.no_book)
        previousDues = findViewById(R.id.dues)
    }

    override fun onResume() {
        super.onResume()
        Log.w("User Class", "Inside user class onResume")
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
            R.id.action_settings -> {
                val i = Intent(this, SettingActivity::class.java)
                startActivity(i)
                true
            }
            R.id.action_logout -> {
                if (checkConnection()) {
                    pb2.visibility = View.VISIBLE
                    LogoutEvent().logoutUser(this)
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

    private fun checkMyPermission(str: String): Boolean {
        val status = ContextCompat.checkSelfPermission(this, str)
        return if (status == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(str), 100)
            false
        } else {
            true
        }
    }

    private fun writeToCalender(bookBeans: BookBeans?) {
        if (checkMyPermission(android.Manifest.permission.WRITE_CALENDAR)) {
            val dueDate = bookBeans?.books!!.dueDate[0].toDate().time - dateConst
            Log.w("Date", "${Date(dueDate)}")
            val content = contentResolver
            val values = ContentValues()
            values.put(CalendarContract.Events.DTSTART, dueDate)
            values.put(CalendarContract.Events.DTEND, dueDate + dateConst)
            values.put(CalendarContract.Events.TITLE, "Due date of ${bookBeans.books.name[0]}")
            values.put(CalendarContract.Events.DESCRIPTION, "Last date to re-issue.")
            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
            values.put(CalendarContract.Events.EVENT_LOCATION, "ITER Library")
            values.put(CalendarContract.Events.CALENDAR_ID, 1)
            values.put(CalendarContract.Events.ALL_DAY, 0)
            values.put(CalendarContract.Events.HAS_ALARM, 1)
            val uri =content.insert(CalendarContract.Events.CONTENT_URI, values)
            long = uri.lastPathSegment.toLong()
            Log.w("writeToCalender", "writeToCalender writing the data $long")
        }
    }

    private fun deleteFromCalendar(bookBeans: BookBeans?) {
        if (checkMyPermission(android.Manifest.permission.READ_CALENDAR)) {
            val uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, long)
            val r = contentResolver.delete(uri, null, null)
            Log.w("deleteFromCalendar", "deleteFromCalendar writing the data $r")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "Can't write to calender", Toast.LENGTH_SHORT).show()
        } else {
            recreate()
        }
    }
}
