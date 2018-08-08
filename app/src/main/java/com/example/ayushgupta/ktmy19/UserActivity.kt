package com.example.ayushgupta.ktmy19

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
import android.view.*
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

    // To set the date and time to 1 day and 7 hours back
    private val dateConst = 61200000L

    // Adapter view in which the books details are shown
    private lateinit var bookView: RecyclerView

    // Just to make sure that the activity is not freeze
    private lateinit var pb2: ProgressBar

    // Numbers of books issued by the student
    private lateinit var noBook: TextView

    // Shows the due amount
    private lateinit var previousDues: TextView

    // Set the email of the user
    private lateinit var email: TextView

    // This method is called after the logout event
    override fun onLogout() {
        pb2.visibility = View.GONE
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    // This is method is called after retrieving the data from the firebase
    override fun setBooks(bookBeans: BookBeans?) {
        //Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
        pb2.visibility = View.GONE
        if (bookBeans == null) {
            Toast.makeText(this, "Error while retrieving the record or your record is empty", Toast.LENGTH_LONG).show()
        } else {
            val books = "No of books ${bookBeans.nBooks}"
            val dues = "Previous dues ${bookBeans.preDues}"

            // Set the numbers of books and dues
            noBook.text = books
            previousDues.text = dues

            // When no book are issued
            if (bookBeans.nBooks == 0) {
                //No books Issued
                Toast.makeText(this, "No books are issued by you", Toast.LENGTH_LONG).show()
            } else {
                // Some books are issued
                val bookAdapter = BookAdapter(bookBeans)
                // Setting books to recycler view
                bookView.adapter = bookAdapter
                // Storing some info
                val spf = getSharedPreferences("Write_to_cal", Context.MODE_PRIVATE)
                // Checking if the user wants the event or not
                val isAllowed = spf.getBoolean("calWrite", true)
                Log.w("IsAllowed", "Inside UserActivity $isAllowed")
                // Checking if the record already exists or not
                val isWritten = spf.getBoolean("isWritten", false)
                Log.w("isWritten", "Inside user class $isWritten")
                //Deleting the record
                if (!isAllowed) {
                    if (isWritten) {
                        deleteFromCalendar(bookBeans)
                        spf.edit().putBoolean("isWritten", false).apply()
                    }
                } else { //Add the Due date to calender
                    if (!isWritten) {
                        writeToCalendar(bookBeans)
                        spf.edit().putBoolean("isWritten", true).apply()
                    }
                }
            }
        }
    }

    // Called when on the creation of the activity
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
        email = nav_view.getHeaderView(0).findViewById(R.id.email)

        bookView = findViewById(R.id.books_view)
        pb2 = findViewById(R.id.pb2)
        noBook = findViewById(R.id.no_book)
        previousDues = findViewById(R.id.dues)
        email.text = intent.getStringExtra("email1")
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
            R.id.setting -> {
                val i = Intent(this, SettingActivity::class.java)
                startActivity(i)
            }
            R.id.logout -> {
                if (checkConnection()) {
                    pb2.visibility = View.VISIBLE
                    LogoutEvent().logoutUser(this)
                } else
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
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

    private fun writeToCalendar(bookBeans: BookBeans?) {
        if (checkMyPermission(android.Manifest.permission.WRITE_CALENDAR)) {
            for (i in 0 until bookBeans?.nBooks!!) {
                val dueDate = bookBeans.books.dueDate[i].toDate().time - dateConst
                Log.w("Date", "${Date(dueDate)}")
                val content = contentResolver
                val values = ContentValues()
                values.put(CalendarContract.Events.DTSTART, dueDate)
                values.put(CalendarContract.Events.DTEND, dueDate + dateConst)
                values.put(CalendarContract.Events.TITLE, "Due date of ${bookBeans.books.name[i]}")
                values.put(CalendarContract.Events.DESCRIPTION, "Last date to re-issue.")
                values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
                values.put(CalendarContract.Events.EVENT_LOCATION, "ITER Library")
                values.put(CalendarContract.Events.CALENDAR_ID, 1)
                values.put(CalendarContract.Events.ALL_DAY, 0)
                values.put(CalendarContract.Events.HAS_ALARM, 1)
                val uri = content.insert(CalendarContract.Events.CONTENT_URI, values)
                val long = uri.lastPathSegment.toLong()
                Log.w("writeToCalendar", "writeToCalendar writing the data $long")
            }
        }
    }

    private fun deleteFromCalendar(bookBeans: BookBeans?) {
        if (checkMyPermission(android.Manifest.permission.READ_CALENDAR)) {
            for (i in 0 until bookBeans?.nBooks!!) {
                //val uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, long)
                val selectionClause = "${CalendarContract.Events.TITLE} = ?"
                val selectionArgs = arrayOf("Due date of ${bookBeans.books.name[i]}")
                val r = contentResolver.delete(CalendarContract.Events.CONTENT_URI, selectionClause, selectionArgs)
                Log.w("deleteFromCalendar", "deleteFromCalendar writing the data $r")
            }
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
