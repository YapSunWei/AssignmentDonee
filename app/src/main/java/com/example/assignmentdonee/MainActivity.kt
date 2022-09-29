package com.example.assignmentdonee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val button: Button = findViewById(R.id.btnAddTest);

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.navrequests -> {
                val intent = Intent(this, RequestManagement::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }


    fun btnAddTest(view: View?) {
        val intent = Intent(this, RequestManagement::class.java)
        startActivity(intent)
        finish()

    }

}